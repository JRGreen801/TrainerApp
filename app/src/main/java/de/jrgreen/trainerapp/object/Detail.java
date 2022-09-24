package de.jrgreen.trainerapp.object;

public class Detail {
    private String _short;
    private String text;

    public Detail(String _short, String text) {
        this._short = _short;
        this.text = text;
    }

    public String get_short() {
        return _short;
    }

    public void set_short(String _short) {
        this._short = _short;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
