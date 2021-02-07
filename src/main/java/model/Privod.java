package model;

public enum Privod {
    Задний,
    Передний,
    Полный;

    @Override
    public String toString() {
        return this.name();
    }
}
