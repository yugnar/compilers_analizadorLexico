package lexer;
public class DecimalNum extends Token {
    public final double value;
    public DecimalNum(double v) { super(Tag.NUM); value = v; }
    public String toString() {
        return super.toString() + "(" + value + ")";
    }
}