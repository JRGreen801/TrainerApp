package de.jrgreen.trainerapp.object;

public class Rating{
    public String _short;
    public int rating;

    public Rating(String _short, int rating) {
        this._short = _short;
        this.rating = rating;
    }

    public Rating(Topic topic, int rating){
        this._short = topic.get_short();
        this.rating = rating;
    }

    public Rating(Detail detail, int rating){
        this._short = detail.get_short();
        this.rating = rating;
    }

    public String get_short() {
        return _short;
    }

    public void set_short(String _short) {
        this._short = _short;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return _short + '=' + rating;
    }
}
