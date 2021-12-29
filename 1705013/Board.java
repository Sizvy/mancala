public class Board {
    int[] player1;
    int[] player2;
    boolean isFree;
    boolean capture;
    double hval;
    int extra;
    int overflow;

    public Board(int[] player1, int[] player2, boolean isFree) {
        this.player1 = player1;
        this.player2 = player2;
        this.isFree = isFree;
    }

    public int[] getPlayer1() {
        return player1;
    }

    public int[] getPlayer2() {
        return player2;
    }

    public void setPlayer1(int[] player1) {
        this.player1 = player1;
    }

    public void setPlayer2(int[] player2) {
        this.player2 = player2;
    }

    public boolean getFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public double getHval() {
        return hval;
    }

    public void setHval(double hval) {
        this.hval = hval;
    }

    public boolean getCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public int getExtra() {
        return extra;
    }

    public int getOverflow() {
        return overflow;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public void setOverflow(int overflow) {
        this.overflow = overflow;
    }
}
