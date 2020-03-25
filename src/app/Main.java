package app;

import data.Data;
import processing.Processor;
import processing.Processors;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Data data = Data.readFromFile("data.csv");
		Processor[] processors = {
				new Processors.PercentScaler(),
				Processors.scale(-20, 123),
				Processors.standardize(),
				Processors.clipLower(-1),
				Processors.clipUpper(1.1),
				Processors.clip(-0.7, 1)
		};
		for (Processor p : processors) {
			System.out.println(String.format("processing data with '%s'", p.getName()));
			System.out.println(String.format("  before: %s", data));
			data = p.process(data);
			System.out.println(String.format("   after: %s", data));
		}
		data.writeToFile("data_processed.csv");
	}
	
}
