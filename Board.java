import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/* Instructions followed: https://bicyclecards.com/how-to-play/solitaire/ */
public class Board {
  
  public static final int padding = 25;

  private final ArrayList<Card> stockPile;
  private final Card[][] tableau;
  private final int[] tableauSizes; // array to stock the index of the last element in all tableau's rows
  private final Card[][] foundations;
  private final int[] foundationsSizes;
  private final Card[] wastePile;
  
  public Board() {
    stockPile = new ArrayList<>();
  
    CardSymbols[] colours = CardSymbols.values();
    CardValues[] values = CardValues.values();
  
    for (CardSymbols colour : colours) {
      for (CardValues value : values) {
        stockPile.add(new Card(value, colour));
      }
    }
    
    tableau = new Card[7][13];
    tableauSizes = new int[7];
    
    foundations = new Card[4][13];
    foundationsSizes = new int[4];
    
    wastePile = new Card[3];
  
    Collections.shuffle(stockPile);

    /* Create the tableau */
    for (int i = 0, n = tableau.length; i < n; i++) {
      // Place one card face up
      Card card = draw();
      moveToTableauRow(card, -1, i);
      card.discover();
      
      for (int j = i + 1; j < n; j++) {
        // Place one card face down
        card = draw();
        moveToTableauRow(card, -1, j);
      }
      
    }
    
  }
  
  private Card draw() {
    if (stockPile.size() > 0) { return stockPile.remove(0); }
    return null;
  }
  
  public void onClick(int mouseX, int mouseY) {
    for (int i = 0, n = tableau.length; i < n; i++) {
      if (tableauSizes[i] == 0) { continue; }
      
      for (int j = 0, m = tableauSizes[i]; j < m; j++) {
  
        Card card = tableau[i][m - 1];
  
        // If I clicked on a card, move it...
        if (card.hover(mouseX, mouseY)) {
    
          int foundationIndex = switch (card.getSymbol()) {
            case "♠" -> 0;
            case "♣" -> 1;
            case "♦" -> 2;
            case "♥" -> 3;
            default -> -1;
          };
    
          int foundationValue = (foundationsSizes[foundationIndex] == 0) ? 0 : foundations[foundationIndex][foundationsSizes[foundationIndex] - 1].getValueNum();
    
          // ...to the right  foundation
          if (card.getValueNum() == foundationValue + 1) {
            card.moveTo(Window.windowWidth - padding - Card.width * (4 - foundationIndex) - padding * (3 - foundationIndex), padding);
            tableauSizes[i]--;
            foundations[foundationIndex][foundationsSizes[foundationIndex]++] = card;
            discoverLastCardOfRow(i);
            card.selected = false;
      
          } else {
      
            // ...to a parent card in another row of the tableau
            for (int k = 0; k < n; k++) {
              if (k == i) { continue; }
              // If it's a King, it can move to an empty spot
              if (card.getValue().equals("K") && tableauSizes[k] == 0) {
                moveToTableauRow(card, i, k);
                discoverLastCardOfRow(i);
                break;
          
                // Otherwise
              } else if (tableauSizes[k] != 0) {
                Card parentCard = tableau[k][tableauSizes[k] - 1];
                if (parentCard.getColour() != card.getColour() && card.getValueNum() == parentCard.getValueNum() - 1) {
                  moveToTableauRow(card, i, k);
                  discoverLastCardOfRow(i);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }
  
  public boolean onMouseMotion(int mouseX, int mouseY) {
    for (int i = 0, n = tableau.length; i < n; i++) {
      if (tableauSizes[i] == 0) { continue; }
      Card card = tableau[i][tableauSizes[i] - 1];
      if (card.hover(mouseX, mouseY) && !card.selected) { card.selected = true; return true; }
      else if (card.selected && !card.hover(mouseX, mouseY)) { card.selected = false; return true; }
    }
    return false;
  }
  
  // Move a given card in the tableau from row number cardRowIndex to row number newRowIndex
  private void moveToTableauRow(Card card, int cardRowIndex, int newRowIndex) {
    int tableauHeight = padding + Card.height + padding * 2;
    card.moveTo(padding + Card.width * newRowIndex + padding * newRowIndex, tableauHeight + padding * tableauSizes[newRowIndex] + padding);
    if (cardRowIndex >= 0) { tableauSizes[cardRowIndex]--; }
    tableau[newRowIndex][tableauSizes[newRowIndex]++] = card;
    card.selected = false;
  }
  
  private void discoverLastCardOfRow(int rowIndex) {
    if (tableauSizes[rowIndex] > 0) { tableau[rowIndex][tableauSizes[rowIndex] - 1].discover(); }
  }
  
  public void paint(Graphics g) {
    // Stock pile
    if (stockPile.size() > 0) {
      g.setColor(Color.pink);
      g.fillRect(padding, padding, Card.width, Card.height);
      g.setColor(Card.borderColour);
      g.drawRect(padding, padding, Card.width, Card.height);
    }
    
    // Tableau
    for (int i = 0, n = tableau.length; i < n; i++) {
      for (int j = 0, m = tableauSizes[i]; j < m; j++) {
        tableau[i][j].paint(g);
      }
    }
    
    // Foundations
    for (int i = 0, n = foundations.length; i < n; i++) {
      // Foundations' border
      g.setColor(Card.borderColour);
      g.drawRect(Window.windowWidth - padding - Card.width * (i + 1) - padding * i, padding, Card.width, Card.height);
      // Foundations' cards
      for (int j = 0, m = foundationsSizes[i]; j < m; j++) {
        foundations[i][j].paint(g);
      }
    }
  }

}