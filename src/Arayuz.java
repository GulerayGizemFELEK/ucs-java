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
		alan.setText("Aranacak nod ad�n� yaz�n ve butona bas�n.");
		
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
		/// DOSYA OKUMA BA�LANGICI
		////////////////////////////////
		try
		{
			FileReader dosyaokuyucu = new FileReader("tree.txt");  
			BufferedReader satirokuyucu = new BufferedReader(dosyaokuyucu);
			
			/// Dosyadan okunan sat�r� tutmak i�in
			String okunan_satir;
			
			/// A�AMA 1 : DOSYADAN T�M NODLARI OKU
			while(true)
			{
				/// Dosyadan satirokuyucu arac�l��� ile bir sat�r okudum
				/// ve bu sat�r� okunan_satir de�i�kenine atad�m
				okunan_satir = satirokuyucu.readLine();
				/// E�er okunan_satir degeri null yani bo� ise
				/// bu dosyan�n sonuna geldi�i, d�ng�y� bitirdim.
				if(okunan_satir == null)
					break;
				/// Okudu�umuz sat�r�(okunan_satir) bo�luk harfine g�re ay�ral�m.
				String[] ayrilan_satir = okunan_satir.split(" ");
				int path_cost = Integer.parseInt(ayrilan_satir[2]);
				
				/* Okudu�umuz birinci ve ikinci harfleri, nod listemiz ile kar��la�t�ral�m.
				 * E�er nod listemizde mevcut de�illerse, bu harfler i�in yeni birer nod yarat�p
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
			/// A�AMA 1 SONU
			
			// Dosyay� ba�a sar
			satirokuyucu.close();
			dosyaokuyucu.close();
			dosyaokuyucu = new FileReader("tree.txt"); 
			satirokuyucu = new BufferedReader(dosyaokuyucu);
			
			// A�AMA 2 : NOD �L��K�LER�N� KUR
			while(true)
			{
				/// Dosyam�zdan satirokuyucu arac�l��� ile bir sat�r okuyal�m
				/// ve bu sat�r� okunan_satir de�i�kenine atayal�m
				okunan_satir = satirokuyucu.readLine();
				/// E�er okunan_satir degeri null yani bo� ise
				/// bu dosyan�n sonuna geldi�imizi belirtir, d�ng�y� bitirelim.
				if(okunan_satir == null)
					break;
				/// Okudu�umuz sat�r�(okunan_satir) bo�luk harfine g�re ay�ral�m.
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

			/// A�AMA 3 : Path cost belirleme a�amas�

				
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
		
			// Nodlar� gezdi�imiz kuyrugun tan�mlanmas�
			Queue<Node> nodKuyrugu = new LinkedList<Node>();
			List<Node> genisletilenNodlar = new ArrayList<Node>();
			/// Nod listesinin en ba��ndaki nodu kuyruga ekle
			nodKuyrugu.add(NodListesi.get(0));

			while(nodKuyrugu.isEmpty() == false && (nodKuyrugu.peek().NodeAdi.compareTo(goalNode) != 0))
			{
				// Kuyru�un en ba��ndaki nodu �ek (en d���k toplam path costlu olan ba�tad�r)
				Node nod = nodKuyrugu.poll();
				// Geni�letilen nod listesine �ekti�imiz nodu ekle
				genisletilenNodlar.add(nod);
				
				// Geni�letti�imiz nodlar� ve daha �nceden kuyrukta olan nodlar�
				// s�ralama i�in bir araya getirebilece�imiz bir liste olu�tural�m
				List<Node> siraliNodListesi = new ArrayList<Node>();
				
				// Geni�letilen nodlar�n alt nodlar�n� s�ralama i�in s�ralama
				// listesine ekle
				for(Node altNod : nod.AltNodlar)
				{
					siraliNodListesi.add(altNod);
					
				}
				
				// �uan kuyrukta olan nodlar� da s�ralama i�in s�ralama 
				// listesine ekle
				while(nodKuyrugu.isEmpty() == false )
				{
					siraliNodListesi.add(nodKuyrugu.poll());
				}
				//s�ralaman�n hepsi sort ile burada yapt�m
				// Listeyi toplam path costa g�re k���kten b�y��e s�rala
				siraliNodListesi.sort(new Comparator<Node>() {
			        @Override
			        public int compare(Node nod1, Node nod2)
			        {
			            return  nod1.ToplamPathCost >= nod2.ToplamPathCost ? 1 : -1;
			        }
			    });
				
				// S�ralanm�� listedeki b�t�n nodlar� nod kuyruguna s�ras�yla ekle
				for(Node siraliNod : siraliNodListesi){
					nodKuyrugu.add(siraliNod);
				}
			}
			
			System.out.println("Geni�letilen nodlar : ");
			alan.setText("");
			alan.setText("Geni�letilen nodlar : \n");
			for(Node gennod : genisletilenNodlar){
				alan.setText(alan.getText() + gennod.NodeAdi);
				System.out.print(gennod.NodeAdi);
			}
			alan.setText(alan.getText()+ "\n");

			
			// Hedef nod, kuyru�un ba��nda olan noddur.
			Node bulunanHedefNod = nodKuyrugu.poll();
			// �imdi, bulunan bu noddan ba�layarak, bu noda
			// nas�l ula�t���m�z� bulal�m. 
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
