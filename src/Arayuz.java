import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Arayuz extends JFrame implements ActionListener{
	
	JTextField Goal = new JTextField("");
	JLabel lbl_hedefNod = new JLabel("Hedef nod : ");
	JButton buton = new JButton("Click Me!");
	JTextArea alan = new JTextArea();
	
	List<Node> NodListesi = new ArrayList<Node>();
	
	public Arayuz(){
		alan.setText("Aranacak nod adýný yazýn ve butona basýn.");
		
		add(lbl_hedefNod);
		add (Goal);
		add (alan);
		add (buton);
		alan.setSize(new Dimension(320,240));
		alan.setPreferredSize(new Dimension(320,240));
		setTitle("Ai proje");
		setLayout (new FlowLayout());
		setSize (350,480);
		setVisible (true) ;
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		Goal.setSize(new Dimension(125,25));
		Goal.setPreferredSize(new Dimension(125,25));
		lbl_hedefNod.setPreferredSize(new Dimension(125,25));
		lbl_hedefNod.setSize(new Dimension(125,25));
		buton.addActionListener(this);
		
		////////////////////////////////
		/// DOSYA OKUMA BAÞLANGICI
		////////////////////////////////
		try
		{
			FileReader dosyaokuyucu = new FileReader("tree.txt");  
			BufferedReader satirokuyucu = new BufferedReader(dosyaokuyucu);
			
			/// Dosyadan okunan satýrý tutmak için
			String okunan_satir;
			
			/// AÞAMA 1 : DOSYADAN TÜM NODLARI OKU
			while(true)
			{
				/// Dosyadan satirokuyucu aracýlýðý ile bir satýr okudum
				/// ve bu satýrý okunan_satir deðiþkenine atadým
				okunan_satir = satirokuyucu.readLine();
				/// Eðer okunan_satir degeri null yani boþ ise
				/// bu dosyanýn sonuna geldiði, döngüyü bitirdim.
				if(okunan_satir == null)
					break;
				/// Okuduðumuz satýrý(okunan_satir) boþluk harfine göre ayýralým.
				String[] ayrilan_satir = okunan_satir.split(" ");
				int path_cost = Integer.parseInt(ayrilan_satir[2]);
				
				/* Okuduðumuz birinci ve ikinci harfleri, nod listemiz ile karþýlaþtýralým.
				 * Eðer nod listemizde mevcut deðillerse, bu harfler için yeni birer nod yaratýp
				 * listemize ekleyelim
				 */
				
				int birinci_varmi = 0;
				int ikinci_varmi = 0;
				for(Node nod : NodListesi)
				{
					if(nod.NodeAdi.compareTo(ayrilan_satir[0]) == 0)
						birinci_varmi = 1;
					if(nod.NodeAdi.compareTo(ayrilan_satir[1]) == 0)
						ikinci_varmi = 1;
				}
				
				if(birinci_varmi == 0)
				{
					Node birinci_nod = new Node();
					birinci_nod.NodeAdi = ayrilan_satir[0];
					//birinci_nod.PathCost = path_cost;
					NodListesi.add(birinci_nod);
				}
				
				if(ikinci_varmi == 0)
				{
					Node ikinci_nod = new Node();
					ikinci_nod.NodeAdi = ayrilan_satir[1];
					ikinci_nod.KendiPathCostu = path_cost;
					
					NodListesi.add(ikinci_nod);
				}	
			}
			/// AÞAMA 1 SONU
			
			// Dosyayý baþa sar
			satirokuyucu.close();
			dosyaokuyucu.close();
			dosyaokuyucu = new FileReader("tree.txt"); 
			satirokuyucu = new BufferedReader(dosyaokuyucu);
			
			// AÞAMA 2 : NOD ÝLÝÞKÝLERÝNÝ KUR
			while(true)
			{
				/// Dosyamýzdan satirokuyucu aracýlýðý ile bir satýr okuyalým
				/// ve bu satýrý okunan_satir deðiþkenine atayalým
				okunan_satir = satirokuyucu.readLine();
				/// Eðer okunan_satir degeri null yani boþ ise
				/// bu dosyanýn sonuna geldiðimizi belirtir, döngüyü bitirelim.
				if(okunan_satir == null)
					break;
				/// Okuduðumuz satýrý(okunan_satir) boþluk harfine göre ayýralým.
				String[] ayrilan_satir = okunan_satir.split(" ");
				String birinci_harf = ayrilan_satir[0];
				String ikinci_harf = ayrilan_satir[1];
				  
				
				//yeni nodlar 
				Node birinci_nod = null;
				Node ikinci_nod = null;
				
				// Birinci nod ve ikinci nodu nod listesinde bul
				for(Node nod : NodListesi) //nod listesindeki her nod icin
				{
					//gezilen nod
					if(nod.NodeAdi.compareTo(birinci_harf) == 0)
						birinci_nod = nod;
					if(nod.NodeAdi.compareTo(ikinci_harf) == 0)
						ikinci_nod = nod;
				}
				
				if(birinci_nod != null)
				{
					if(ikinci_nod != null)
					{
						
						ikinci_nod.SahipNod = birinci_nod;
						birinci_nod.AltNodlar.add(ikinci_nod);
					}
				}
			}
			
			satirokuyucu.close();
			dosyaokuyucu.close();

			/// AÞAMA 3 : Path cost belirleme aþamasý

				
			for(Node nod : NodListesi)
			{
				Node parent_nod = nod.SahipNod;
				
				nod.ToplamPathCost +=  nod.KendiPathCostu; // kendi pathcostunu ekledim toplam0 
				while(parent_nod != null){
					nod.ToplamPathCost += parent_nod.KendiPathCostu;
					parent_nod = parent_nod.SahipNod;
				}
			}
			
		}   catch(Exception ex)
		{

		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Arayuz();
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == buton)
		{
			/// Uniform cost search
			String goalNode = Goal.getText(); // Hedef nodu al
		
			// Nodlarý gezdiðimiz kuyrugun tanýmlanmasý
			Queue<Node> nodKuyrugu = new LinkedList<Node>();
			List<Node> genisletilenNodlar = new ArrayList<Node>();
			/// Nod listesinin en baþýndaki nodu kuyruga ekle
			nodKuyrugu.add(NodListesi.get(0));

			while(nodKuyrugu.isEmpty() == false && (nodKuyrugu.peek().NodeAdi.compareTo(goalNode) != 0))
			{
				// Kuyruðun en baþýndaki nodu çek (en düþük toplam path costlu olan baþtadýr)
				Node nod = nodKuyrugu.poll();
				// Geniþletilen nod listesine çektiðimiz nodu ekle
				genisletilenNodlar.add(nod);
				
				// Geniþlettiðimiz nodlarý ve daha önceden kuyrukta olan nodlarý
				// sýralama için bir araya getirebileceðimiz bir liste oluþturalým
				List<Node> siraliNodListesi = new ArrayList<Node>();
				
				// Geniþletilen nodlarýn alt nodlarýný sýralama için sýralama
				// listesine ekle
				for(Node altNod : nod.AltNodlar)
				{
					siraliNodListesi.add(altNod);
					
				}
				
				// Þuan kuyrukta olan nodlarý da sýralama için sýralama 
				// listesine ekle
				while(nodKuyrugu.isEmpty() == false )
				{
					siraliNodListesi.add(nodKuyrugu.poll());
				}
				//sýralamanýn hepsi sort ile burada yaptým
				// Listeyi toplam path costa göre küçükten büyüðe sýrala
				siraliNodListesi.sort(new Comparator<Node>() {
			        @Override
			        public int compare(Node nod1, Node nod2)
			        {
			            return  nod1.ToplamPathCost >= nod2.ToplamPathCost ? 1 : -1;
			        }
			    });
				
				// Sýralanmýþ listedeki bütün nodlarý nod kuyruguna sýrasýyla ekle
				for(Node siraliNod : siraliNodListesi){
					nodKuyrugu.add(siraliNod);
				}
			}
			
			System.out.println("Geniþletilen nodlar : ");
			alan.setText("");
			alan.setText("Geniþletilen nodlar : \n");
			for(Node gennod : genisletilenNodlar){
				alan.setText(alan.getText() + gennod.NodeAdi);
				System.out.print(gennod.NodeAdi);
			}
			alan.setText(alan.getText()+ "\n");

			
			// Hedef nod, kuyruðun baþýnda olan noddur.
			Node bulunanHedefNod = nodKuyrugu.poll();
			// Þimdi, bulunan bu noddan baþlayarak, bu noda
			// nasýl ulaþtýðýmýzý bulalým. 
			StringBuilder yol = new StringBuilder();
			Node gezici = bulunanHedefNod;
			
			while(gezici != null){
			
				yol.append(gezici.NodeAdi);
				
				gezici = gezici.SahipNod;
			}
			
			yol.reverse();
			System.out.println("Yol : " + yol.toString());
			alan.setText(alan.getText() + "Yol : \n");
			alan.setText(alan.getText() + yol.toString()+ "\n");
			
			
		}
		
	}

}
