package pl.simulation.ocean.testutil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Rejestruje {@link System#out} podczas testów, które powodują wygenerowanie komunikatów w konsoli */
public final class SystemOutSilencer implements AutoCloseable {

    private final PrintStream original;
    private final ByteArrayOutputStream buffer;

    public SystemOutSilencer() {
        original = System.out;
        buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    public String captured() {
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
        System.setOut(original);
    }
}
