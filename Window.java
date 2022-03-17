import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JPanel {
  
  private final Board board;
  
  private static final int offset = 15; // Without the offset, a black rectangle appears on the right side of the window because it is not large enough
  public static final int windowWidth = Board.padding + Card.width * 7 + Board.padding * 7 + offset;
  public static final int windowHeight = Board.padding + Card.height + Board.padding * 2 + Board.padding * 12 + Card.height + Board.padding;
  
  public Window() {
    super();
    
    this.board = new Board();
    
    setBackground(Color.GREEN.darker().darker().darker().darker()); // Background color
    JFrame frame = new JFrame("Solitaire"); // Window title
    frame.setSize(windowWidth, windowHeight);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Window closable
    frame.getContentPane().add(this); // Add window to pane
    
    /* Put window on middle of the screen */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); // Doesn't take account of the taskbar on the side
    Dimension frameSize = frame.getSize();
    frame.setLocation((screen.width - frameSize.width)/2, (screen.height - frameSize.height)/2);
    
    frame.setVisible(true);
  
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        board.onClick(e.getX(), e.getY());
        repaint();
      }
    });
    
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        if (board.onMouseMotion(e.getX(), e.getY())) { repaint(); }
      }
    });
  }
  
  // Called by the runtime system whenever the panel needs painting.
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    board.paint(g);
  }

}