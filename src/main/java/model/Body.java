package model;

public enum Body {
    Хэчбэк,
    Седан,
    Универсал,
    Кросовер,
    Внедорожник,
    Пикап,
    Фургон;

    @Override
    public String toString() {
        return this.name();
    }
}
