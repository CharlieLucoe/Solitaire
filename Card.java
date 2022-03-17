import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

/* https://www.dcode.fr/playing-cards */
public class Card {

  private final CardSymbols symbol;
  private CardValues value;
  
  public static final Color backColour = Color.BLUE.darker().darker().darker();
  public static final Color borderColour = Color.CYAN;
  public static final Color selectedBorderColour = Color.ORANGE;

  private boolean discovered = false; // If a card is discovered, it is facing up, otherwise it faces down
  public boolean selected = false;

  /* Position */
  private int x;
  private int y;

  public static final int width = 135;
  public static final int height = 200;

  public Card(CardValues value, CardSymbols symbol) {
    this.value = value;
    this.symbol = symbol;
  }
  
  public void discover() { if (!discovered) { discovered = true; } }
  
  public void moveTo(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public String getValue() {
    return switch (value) {
      case ACE -> "A";
      case TWO -> "2";
      case THREE -> "3";
      case FOUR -> "4";
      case FIVE -> "5";
      case SIX -> "6";
      case SEVEN -> "7";
      case EIGHT -> "8";
      case NINE -> "9";
      case TEN -> "10";
      case JACK -> "J";
      case QUEEN -> "Q";
      case KING -> "K";
    };
  }
  
  public String getSymbol() {
    return switch (symbol) {
      case SPADES -> "♠";
      case CLUBS -> "♣";
      case DIAMONDS -> "♦";
      case HEARTS -> "♥";
    };
  }
  
  public int getValueNum() {
    return switch (value) {
      case ACE -> 1;
      case TWO -> 2;
      case THREE -> 3;
      case FOUR -> 4;
      case FIVE -> 5;
      case SIX -> 6;
      case SEVEN -> 7;
      case EIGHT -> 8;
      case NINE -> 9;
      case TEN -> 10;
      case JACK -> 11;
      case QUEEN -> 12;
      case KING -> 13;
    };
  }
  
  public Color getColour() {
    return ((symbol == CardSymbols.CLUBS || symbol == CardSymbols.SPADES) ? Color.BLACK : Color.RED);
  }
  
  public boolean hover(int mouseX, int mouseY) {
    return ((mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height));
  }

  public void paint(Graphics g) {
    // Card's colour
    Color colour = (discovered) ? getColour() : backColour;
    g.setColor(colour);
    g.fillRect(x, y, width, height);
    // Card's border
    g.setColor((selected) ? selectedBorderColour : borderColour);
    g.drawRect(x, y, width, height);
    // Card's value
    if (discovered) {
      g.setColor((colour == Color.BLACK) ? Color.WHITE : Color.BLACK);
      Font font = new Font("Times New Roman", Font.BOLD, Board.padding);
      g.setFont(font);
      g.drawString(getValue() + " " + getSymbol(), x + 10, y + Board.padding);
    }
  }
  
  public void setValue(CardValues newValue) { value = newValue; }

}