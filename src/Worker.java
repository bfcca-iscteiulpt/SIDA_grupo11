import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Worker extends Thread {

	private String dados;
	private File ficheiro;
	
	public Worker(File ficheiro, String dados){
		this.ficheiro = ficheiro;
		this.dados = dados;
	}
	
	@Override
	public void run(){
		
		//Escrever no ficheiro recebido os dados recebidos

		try {
		
				
			FileWriter fw = new FileWriter(ficheiro,true);
			fw.append(dados + "\r\n");
			fw.close();
				
		} catch (IOException e) {}
	}
	
}