package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.ComponentRemotePacker;
import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerClusterInputStream;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.exception.ClusterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Поведение {@link RemotePackerClusterInputStream} при ошибках сериализации/десериализации:
 * исключение должно строиться через {@code Cluster.getExceptionBuilder()} и доходить до
 * {@link ComponentRemotePacker} в распознаваемой форме.
 */
public class RemotePackerClusterInputStreamThrowTest {

    /**
     * Несуществующая нода в Packer'е — {@link RemotePackerClusterInputStream#getInputStream}
     * бросает исключение, построенное дефолтным {@code ExceptionBuilderImpl}.
     * Это {@link ClusterException} с сообщением, идентифицирующим конкретные node/component id —
     * то есть путь проходит через билдер, а не через no-arg {@code new ClusterRemotePackerException()}.
     */
    @Test
    public void unknownRemoteNode_throwsViaExceptionBuilder() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();
            Cluster cluster2 = clusters.getCluster2();
            Custom1Component component = cluster1.getAnyLocalComponent(Custom1Component.class);

            // nodeRuntimeId — существующий (cluster2), componentId — заведомо несуществующий:
            // это попадает в ветку runtimeComponentInfo == null именно по componentId,
            // моделируя ситуацию «компонент на удалённой ноде уже снят / нода ещё не зарегистрировала его».
            UUID knownNodeRuntimeId = cluster2.node.getRuntimeId();
            int unknownComponentId = 999_999;
            byte[] dummyData = new byte[ClusterInputStream.BATCH_SIZE];
            int validId = 42;

            RemotePackerClusterInputStream.Packer packer = new RemotePackerClusterInputStream.Packer(
                    knownNodeRuntimeId, unknownComponentId, validId, ClusterInputStream.BATCH_SIZE,
                    dummyData, 0, dummyData.length
            );

            ClusterException thrown = Assertions.assertThrows(
                    ClusterException.class,
                    () -> RemotePackerClusterInputStream.getInputStream(component, packer)
            );
            Assertions.assertNotNull(thrown.getMessage(),
                    "Билдер должен заполнить сообщение, в отличие от прежнего new ClusterRemotePackerException()");
            Assertions.assertTrue(thrown.getMessage().contains(knownNodeRuntimeId.toString()),
                    "Сообщение должно содержать sourceNodeRuntimeId: " + thrown.getMessage());
            Assertions.assertTrue(thrown.getMessage().contains(String.valueOf(unknownComponentId)),
                    "Сообщение должно содержать sourceComponentId: " + thrown.getMessage());
        }
    }

    /**
     * {@code IOException} с «ожидаемым» cause перепаковывается так, что cause —
     * прямой cause {@link RuntimeException}.
     */
    @Test
    public void serialize_ioExceptionWithExpectedCause_unwrapsToRuntimeException() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Custom1Component component = clusters.getCluster1().getAnyLocalComponent(Custom1Component.class);

            ClusterException expectedCause = new ClusterException("expected by ExceptionBuilder");
            ClusterInputStream stream = new ClusterInputStream(throwingInputStream(new IOException("io wrap", expectedCause)));

            RuntimeException thrown = Assertions.assertThrows(
                    RuntimeException.class,
                    () -> new RemotePackerClusterInputStream().serialize(component, stream)
            );
            Assertions.assertSame(expectedCause, thrown.getCause(),
                    "Ожидаемое исключение должно быть прямым cause RuntimeException, не глубже");
        }
    }

    /**
     * {@code IOException} без «ожидаемого» cause — поведение прежнее:
     * cause {@link RuntimeException} — исходная {@link IOException}.
     */
    @Test
    public void serialize_ioExceptionWithUnexpectedCause_keepsLegacyWrapping() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Custom1Component component = clusters.getCluster1().getAnyLocalComponent(Custom1Component.class);

            IOException rawIo = new IOException("io wrap", new IllegalStateException("not expected"));
            ClusterInputStream stream = new ClusterInputStream(throwingInputStream(rawIo));

            RuntimeException thrown = Assertions.assertThrows(
                    RuntimeException.class,
                    () -> new RemotePackerClusterInputStream().serialize(component, stream)
            );
            Assertions.assertSame(rawIo, thrown.getCause(),
                    "При не-ожидаемом cause поведение прежнее: RuntimeException(originalIOException)");
        }
    }

    /**
     * UEH-overload {@link ComponentRemotePacker#serialize}: «ожидаемый» cause пробрасывается,
     * UEH не вызывается.
     */
    @Test
    public void componentRemotePacker_serializeUEH_unwrapsExpectedCause_andSkipsUEH() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Custom1Component component = clusters.getCluster1().getAnyLocalComponent(Custom1Component.class);
            ComponentRemotePacker packer = component.getRemotes().getRemotePackerObjects();

            ClusterException expectedCause = new ClusterException("expected by ExceptionBuilder");
            ClusterInputStream stream = new ClusterInputStream(throwingInputStream(new IOException("io wrap", expectedCause)));

            AtomicReference<Throwable> uehCaught = new AtomicReference<>();
            Thread.UncaughtExceptionHandler ueh = (t, ex) -> uehCaught.set(ex);

            ClusterException thrown = Assertions.assertThrows(
                    ClusterException.class,
                    () -> packer.serialize(ClusterInputStream.class, stream, ueh)
            );
            Assertions.assertSame(expectedCause, thrown,
                    "Должно быть выброшено само ожидаемое исключение, а не обёртка");
            Assertions.assertNull(uehCaught.get(),
                    "UEH не должен вызываться, когда исключение распознано как ожидаемое");
        }
    }

    /**
     * UEH-overload {@link ComponentRemotePacker#serialize}: не-«ожидаемый» cause → возврат
     * {@code null}, UEH вызван.
     */
    @Test
    public void componentRemotePacker_serializeUEH_unexpectedCause_invokesUEH_andReturnsNull() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Custom1Component component = clusters.getCluster1().getAnyLocalComponent(Custom1Component.class);
            ComponentRemotePacker packer = component.getRemotes().getRemotePackerObjects();

            IOException rawIo = new IOException("io wrap", new IllegalStateException("not expected"));
            ClusterInputStream stream = new ClusterInputStream(throwingInputStream(rawIo));

            AtomicReference<Throwable> uehCaught = new AtomicReference<>();
            Thread.UncaughtExceptionHandler ueh = (t, ex) -> uehCaught.set(ex);

            byte[] result = packer.serialize(ClusterInputStream.class, stream, ueh);

            Assertions.assertNull(result, "При не-ожидаемом исключении возвращается null");
            Assertions.assertNotNull(uehCaught.get(), "UEH должен быть вызван");
            Assertions.assertSame(rawIo, uehCaught.get().getCause(),
                    "В UEH прилетает оригинальный RuntimeException(originalIOException)");
        }
    }

    private static InputStream throwingInputStream(IOException toThrow) {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                throw toThrow;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw toThrow;
            }
        };
    }
}
