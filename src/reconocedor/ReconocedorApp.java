package reconocedor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class ReconocedorApp {
	
	private AudioFormat format;

	// path of the wav file
	File wavFile = new File("/Users/diegofarfan/Desktop/ModeladoHTK/pruebaApp/RecordAudio.wav");

	// format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// the line from which audio data is captured
	TargetDataLine line;
	
	//Random number of questions
	final int[] numbers;
	private ArrayList<String> preguntas;
	private ArrayList<String> respuestas;	

	public ReconocedorApp() {
		format = getAudioFormat();
		numbers = new Random().ints(0, 20).distinct().limit(10).toArray();
		preguntas = new ArrayList<String>();
		respuestas = new ArrayList<String>();
		getQuestionsFromFile();
	}
	
	public void getQuestionsFromFile() {
		try {
			String line = null;
			FileReader fileReader = new FileReader("res/preguntas.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String [] segmentos = line.split("-");
	            preguntas.add(segmentos[0]);
	            respuestas.add(segmentos[1]);
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getQuestion(int numeroPregunta) {
		String[] response = new String[2];
		response[0] = preguntas.get(numbers[numeroPregunta]);
		response[1] = respuestas.get(numbers[numeroPregunta]);
		
		return response;
	}
	
	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}
	
	/**
	 * Captures the sound and record into a WAV file
	 */
	void start() {
		try {
			
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start(); // start capturing

			System.out.println("Start capturing...");

			AudioInputStream ais = new AudioInputStream(line);

			System.out.println("Start recording...");

			// start recording
			AudioSystem.write(ais, fileType, wavFile);

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		line.stop();
		line.close();
		System.out.println("Finished");
	}

	public void iniciarGrabacion() {
		
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				start();
			}
		});

		stopper.start();
	}

	public void moverArchivoLocal() {
		// Accedo a archivo local en Resources y muevo a carpeta deseada
		ClassLoader classLoader = getClass().getClassLoader();
		File file;
		try {
			file = new File(classLoader.getResource("uno.wav").getFile());
			System.out.println(file.getAbsolutePath());
			if (file.renameTo(new File("/Users/diegofarfan/Desktop/ModeladoHTK/pruebaApp/" + file.getName()))) {
				System.out.println("File is moved successful!");
			} else {
				System.out.println("File is failed to move!");
			}
			System.out.println(file.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void obtenerMFCC() {
		// Genero MFC de archivo de audio
		Process cmdProc;
		try {
			cmdProc = Runtime.getRuntime()
					.exec(new String[] { "/bin/bash", "-c",
							"hcopy -C SampleData/wav2MFCC_config pruebaApp/RecordAudio.wav pruebaApp/RecordAudio.mfc" },
							new String[] {}, new File("/Users/diegofarfan/Desktop/ModeladoHTK/"));
			cmdProc.waitFor();
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
			String line;
			while ((line = stdoutReader.readLine()) != null) {
				// process procs standard output here
				System.out.println("Standard Output: " + line);
			}
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
			while ((line = stderrReader.readLine()) != null) {
				System.out.println("Standard Error: " + line);
				// process procs standard error here
			}
			int retValue = cmdProc.exitValue();
			System.out.println("Return value: " + retValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void obtenerResultados() {
		// Prueba modelo
		Process cmdProc;
		try {
			cmdProc = Runtime.getRuntime().exec(
					new String[] { "/bin/bash", "-c",
							"hvite -H Model/hmm3/hmmdefs -H Model/hmm3/macros "
									+ "-w Grammar/network.slf Grammar/versionFonemas/phones.dict "
									+ "Grammar/versionFonemas/phones.list pruebaApp/RecordAudio.mfc" },
					new String[] {}, new File("/Users/diegofarfan/Desktop/ModeladoHTK/"));
			cmdProc.waitFor();
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
			String line;
			while ((line = stdoutReader.readLine()) != null) {
				// process procs standard output here
				System.out.println("Standard Output: " + line);
			}
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
			while ((line = stderrReader.readLine()) != null) {
				System.out.println("Standard Error: " + line);
				// process procs standard error here
			}
			int retValue = cmdProc.exitValue();
			System.out.println("Return value: " + retValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String leerResultado() {
		String fileName = "/Users/diegofarfan/Desktop/ModeladoHTK/pruebaApp/RecordAudio.rec";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] array = line.split(" ");
				System.out.println(array[2]);
				return array[2];
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Intenta nuevamente";
	}

}
