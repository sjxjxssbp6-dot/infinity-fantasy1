package cupcake.nikky.imperium.tower.user;

public interface UserState {

    boolean isInvalidated();

    void setInvalidated(boolean invalidated);

    boolean isLoaded();

    void setLoaded(boolean loaded);

}
