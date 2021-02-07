package model;

public enum Gear {
    Ручная,
    Автомат,
    Робот,
    Вариатор;

    @Override
    public String toString() {
        return this.name();
    }
}
