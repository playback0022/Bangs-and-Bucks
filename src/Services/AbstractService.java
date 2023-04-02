package Services;

public abstract class AbstractService {
    protected abstract void printAllEntities();
    protected abstract void printEntity();
    protected abstract void registerEntity();
    protected abstract void unregisterEntity();
    public abstract void initService();
}
