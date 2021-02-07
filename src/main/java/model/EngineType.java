package model;

public enum EngineType {
    Бензиновый,
    Дизельный,
    Газ,
    Электрический;

    @Override
    public String toString() {
        return this.name();
    }
}
