import java.util.ArrayList;
import java.util.List;

public class Node {
	public List<Node> AltNodlar = new ArrayList<Node>(); //alt nodlear listesi 
    public String NodeAdi ;
    public Node SahipNod = null;
    public int KendiPathCostu = 0;
    public int ToplamPathCost;
   
}
