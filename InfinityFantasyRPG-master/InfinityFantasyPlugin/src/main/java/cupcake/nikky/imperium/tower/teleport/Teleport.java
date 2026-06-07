package cupcake.nikky.imperium.tower.teleport;

public interface Teleport {

    void process(float seconds, int cost);

    void cancel(float seconds);

}
