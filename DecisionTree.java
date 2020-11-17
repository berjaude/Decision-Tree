package edu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DecisionTree {
	private List<Attribute> attributes;
	private List<String[]> matrix;
	private List<int[][]> phases;
	private int classifications[];
	private int attrSize;
	private int cases, size;

	public DecisionTree(String meta, String train) throws FileNotFoundException {
		Scanner m = new Scanner(new File(meta));
		attributes = new ArrayList<>();
		matrix = new ArrayList<>();
		phases = new ArrayList<>();
		cases = 0;

		System.out.println("Meta fFile : " + meta);
		while (m.hasNextLine()) {
			String temp = m.nextLine();
			int find = temp.indexOf(":");
			String key = temp.substring(0, find);
			String[] value = temp.substring(find + 1).split(",");
			Attribute att = new Attribute(key, value);
			attributes.add(att);
		}
		// Once we have the meta data, we can initialize classification array
		// having the same size as the classification array of string
		attrSize = attributes.size();
		size = attributes.get(attrSize - 1).getValue().length; // # of classifications
		classifications = new int[size];
		// System.out.println(size);

		System.out.println("Training file : " + train);
		addTrainingData(train);

		// close scanners
		m.close();
	}

	public void addTrainingData(String train) throws FileNotFoundException {
		Scanner t = new Scanner(new File(train));
		while (t.hasNextLine()) {
			String temp = t.nextLine();
			String[] line = temp.split(",");
			int len = line.length;
			if (len == attributes.size()) {
				matrix.add(line);
				for (int i = 0; i < size; i++) {
					if (attributes.get(attrSize - 1).getValue()[i].equals(line[len - 1])) {
						classifications[i] += 1;
					}
				}
				// increment cases' number
				cases++;
			}
		}
		t.close();
	}

	public void printAttributes() {
		System.out.println("There are " + (attributes.size() - 1) + " attribute(s)");
		for (int i = 0; i < attributes.size() - 1; i++) {
			System.out.print("\t" + attributes.get(i).getKey() + " : ");
			for (String s : attributes.get(i).getValue())
				System.out.print(s + " ");
			System.out.println();
		}
		int temp = attributes.size() - 1; // case for classification
		System.out.print("Classified under " + attributes.get(temp).getKey() + " as: ");
		for (String s : attributes.get(temp).getValue())
			System.out.print(s + " ");
		System.out.println();

		System.out.println("\nClassifications's repartition:");
		for (int i = 0; i < classifications.length; i++) {
			System.out.println(attributes.get(attrSize - 1).getValue()[i] + " = " + classifications[i] + " / " + cases);
		}
	}

	public void printRawData() {
		System.out.println("Training data");
		for (int i = 0; i < matrix.size(); i++) {
			for (String l : matrix.get(i))
				System.out.print(l + " ");
			System.out.println();
		}
	}

	public void createLearningPhase() {
		int cl = attributes.size() - 1; // index to access classification slot
		for (int i = 0; i < attributes.size() - 1; i++) {
			int row = attributes.get(i).getValue().length;
			int col = classifications.length;
			int temp[][] = new int[row][col];
			for (int j = 0; j < matrix.size(); j++) {
				for (int k = 0; k < col; k++) {
					for (int r = 0; r < row; r++) {
						if (matrix.get(j)[i].equals(attributes.get(i).getValue()[r])
								&& matrix.get(j)[cl].equals(attributes.get(cl).getValue()[k]))
							temp[r][k] += 1;
					}
				}
			}
			phases.add(temp);
		}
	}

	public void printPhases() {
		for (int i = 0; i < phases.size(); i++) {
			System.out.println("Phase " + attributes.get(i).getKey());
			System.out.print("\t");
			for (String s : attributes.get(attributes.size() - 1).getValue())
				System.out.print(s + "\t");
			System.out.println();
			for (int[] j : phases.get(i)) {
				System.out.print("\t");
				for (int k : j)
					System.out.print(k + "\t");
				System.out.println();
			}
			System.out.println();
		}
	}

	private static float log2(float num) {
		float ret = 0.0f;
		if (num > 0.0)
			ret = (float) (Math.log10(num) / Math.log10(2));
		return ret;
	}

	public void getSelection() {
		float info = 0.0f;
		List<Pick> gain = new ArrayList<>();
		for (float c : classifications) {
			info += -1 * (c / cases) * log2(c / cases);
		}
		System.out.format("Entropy = %.3f bits\n", info);

		for (int i = 0; i < phases.size(); i++) {
			float infoAt = 0.0f;
			for (int[] j : phases.get(i)) {
				float sum = 0.0f;
				float local = 0.0f;
				for (int k : j)
					sum += k;
				// System.out.println("Sum is " + sum);
				for (float k : j) {
					local += -1 * (k / sum) * log2(k / sum);
				}
				// System.out.println("local " + local);
				local *= (sum / cases);
				// System.out.println("local div " + sum/cases);
				infoAt += local;
			}
			System.out.format("Info_%s(D) = %.3f bits\n", attributes.get(i).getKey(), infoAt);
			gain.add(new Pick(info - infoAt, i));
		}

		gain.sort(Collections.reverseOrder(new PickComparator()));
		System.out.format("\nDecision made using %s with %.3f bits\n", attributes.get(gain.get(0).getPhase()).getKey(),
				gain.get(0).getGain());
	}

	public static void main(String[] args) throws IOException {

		DecisionTree classify = new DecisionTree("car.meta.txt", "car.train.txt");
		System.out.println();
		classify.printAttributes();
		System.out.println();
		classify.createLearningPhase();
		System.out.println();
		classify.printPhases();
		System.out.println();
		classify.getSelection();

//		Scanner sc1 = new Scanner(System.in);
//		Scanner sc2 = new Scanner(System.in);
//		Scanner sc3 = new Scanner(System.in);
//		Scanner sc4 = new Scanner(System.in);
//		
//		boolean outter = false;
//		String temp = "";
//		do {
//			
//		}while(outter);
//
//		// close scanners
//		sc1.close();
//		sc2.close();
//		sc3.close();
//		sc4.close();
	}
}