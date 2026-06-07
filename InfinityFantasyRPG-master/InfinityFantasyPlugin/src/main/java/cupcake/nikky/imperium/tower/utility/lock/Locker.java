package cupcake.nikky.imperium.tower.utility.lock;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@NoArgsConstructor(staticName = "create")
public class Locker extends ReentrantReadWriteLock {

    public <T> T read(@NotNull final LockerProcess<T> process) {
        this.readLock().lock();
        try {
            return process.process();
        } finally {
            this.readLock().unlock();
        }
    }

    public <T> T write(@NotNull final LockerProcess<T> process) {
        this.writeLock().lock();
        try {
            return process.process();
        } finally {
            this.writeLock().unlock();
        }
    }

    public void readPure(@NotNull final LockerPureProcess process) {
        this.readLock().lock();
        try {
            process.process();
        } finally {
            this.readLock().unlock();
        }
    }

    public void writePure(@NotNull final LockerPureProcess process) {
        this.writeLock().lock();
        try {
            process.process();
        } finally {
            this.writeLock().unlock();
        }
    }

}
