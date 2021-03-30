import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route {
    int flag=2;
    int counter=0;
    String firstNode="";
    double timeTransmission=0.0;
    double timeTransmissionNext=0.0;
    int errorPacketCounter=0;
    ArrayList<String> nodes = new ArrayList<String>();
    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<Line> lines = new ArrayList<Line>();
    Route() throws FileNotFoundException {
        createNodes();
        createPaths();
        deleteFiles();
        for(int j=0; j<paths.size(); j++){
            String dirPath = paths.get(j);
            for(int i =1; i<7; i++){
                readData(dirPath,i);
            }
            readRoutes(dirPath+"\\w1_do_w5");
            writeStatistics(dirPath+"\\w1_do_w5");
            readRoutes(dirPath+"\\w5_do_w1");
            writeStatistics(dirPath+"\\w5_do_w1");
        }
    }

    public void readData(String dirPath,int fileNumber) throws FileNotFoundException {
        try {
            File myObj = new File(getFilePath(dirPath,fileNumber));
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                findRoute(data, fileNumber, dirPath);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void findRoute(String line, int choice, String dirPath ){
        if(line.contains("RR:")) {
            if(flag==0){
                double avg = timeTransmission/counter;
                writeToFile(counter+";",dirPath,choice);
                writeToFile(avg+";",dirPath,choice);
                writeToFile(errorPacketCounter+"\n",dirPath,choice);
                timeTransmission=0.0;
            }
            timeTransmission=timeTransmissionNext;
            flag=1;
            errorPacketCounter=0;
            counter = 1;
            for (int i = 0; i < nodes.size(); i++) {
                if (line.contains(nodes.get(i))) {
                    firstNode=nodes.get(i);
                    writeToFile(firstNode+"-",dirPath,choice);
                }
            }
        }
        else if(flag==1){
            if(line.contains(firstNode)){
                flag=0;
                writeToFile(firstNode+";",dirPath,choice);
            }
            else{
                for(int i = 0; i<nodes.size(); i++){
                    if (line.contains(nodes.get(i))) {
                        writeToFile(nodes.get(i)+"-",dirPath,choice);
                    }
                }
            }
        }
        else if(line.contains("(same route)")){
            counter++;
            Pattern pattern = Pattern.compile("([0-9]{1,3}[.][0-9]{1,2})");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find())
            {
                timeTransmission += Double.parseDouble(matcher.group(1));
            }
        }
        else if( line.contains("bytes from raspberry")){
            Pattern pattern = Pattern.compile("([0-9]{1,3}[.][0-9]{1,2})");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find())
            {
                timeTransmissionNext = Double.parseDouble(matcher.group(1));
            }
        }
        else if( line.contains("timed out")){
            errorPacketCounter++;
        }
        else if(line.contains("ping statistics")){
            double avg = timeTransmission/counter;
            writeToFile(counter+";",dirPath,choice);
            writeToFile(avg+";",dirPath,choice);
            writeToFile(errorPacketCounter+"\n",dirPath,choice);
            flag=2;
            errorPacketCounter =0;
        }


    }

    public void writeToFile(String data, String dirPath,int choice){
        try {
            FileWriter myWriter = new FileWriter(mapPath(dirPath,choice), true);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void createNodes(){
        nodes.add("raspberry1");
        nodes.add("raspberry2");
        nodes.add("raspberry3");
        nodes.add("raspberry4");
        nodes.add("raspberry5");
    }

    public void createPaths(){
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_IV\\W_2");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_IV\\W_3");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_IV\\W_4");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_IV\\W_5");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_V\\W_2");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_V\\W_3");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_V\\W_4");
        paths.add("C:\\Users\\radek\\Desktop\\TESTY\\USTAWIENIE W LINII\\PING\\BATMAN_V\\W_5");
    }

    public String mapPath(String dirPath,int choice){
        switch (choice){
            case 1:
                return dirPath + "\\" + "w1_do_w5";
            case 2:
                return dirPath + "\\" + "w1_do_w5";
            case 3:
                return dirPath + "\\" + "w1_do_w5";
            case 4:
                return dirPath + "\\" + "w5_do_w1";
            case 5:
                return dirPath + "\\" + "w5_do_w1";
            case 6:
                return dirPath + "\\" + "w5_do_w1";
        }
        return "Błąd";
    }

    public String getFilePath(String dirPath, int choice){
        String ping_w1_1 = "ping_w1_1";
        String ping_w1_2 = "ping_w1_2";
        String ping_w1_3 = "ping_w1_3";
        String ping_w5_1 = "ping_w5_1";
        String ping_w5_2 = "ping_w5_2";
        String ping_w5_3 = "ping_w5_3";
        switch (choice){
            case 1:
                return dirPath + "\\" + ping_w1_1;
            case 2:
                return dirPath + "\\" + ping_w1_2;
            case 3:
                return dirPath + "\\" + ping_w1_3;
            case 4:
                return dirPath + "\\" + ping_w5_1;
            case 5:
                return dirPath + "\\" + ping_w5_2;
            case 6:
                return dirPath + "\\" + ping_w5_3;
        }
        return "Błąd";
    }

    public void readRoutes(String filePath){
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                buildStatistics(data);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void buildStatistics(String filePath){
        int match =0;
        String[] result = filePath.split(";");
        for (int i =0; i<lines.size(); i++){
            if(result[0].equals(lines.get(i).route)){
                lines.get(i).occurances += Integer.parseInt(result[1]);
                lines.get(i).timeAvg = (lines.get(i).timeAvg + Double.parseDouble(result[2]))/2;
                lines.get(i).errorPacket += Integer.parseInt(result[3]);
                match=1;
            }
        }
        if(match==0){
            lines.add(new Line(result[0],Integer.parseInt(result[1]),Double.parseDouble(result[2]),Integer.parseInt(result[3])));
        }

    }
    public void writeStatistics(String filePath){
        try {
            String data;
            FileWriter myWriter = new FileWriter(filePath+".csv", true);
            for (int i =0; i<lines.size(); i++){
                data = lines.get(i).route+";"+lines.get(i).occurances+";"+lines.get(i).timeAvg+";"+lines.get(i).errorPacket+"\n";
                myWriter.write(data);
            }
            lines.clear();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void deleteFiles(){

        for(int j=0; j<paths.size(); j++){
            String dirPath = paths.get(j);
            File myObj = new File(dirPath+"\\w1_do_w5");
            myObj.delete();
            myObj = new File(dirPath+"\\w5_do_w1");
            myObj.delete();
            myObj = new File(dirPath+"\\w1_do_w5.csv");
            myObj.delete();
            myObj = new File(dirPath+"\\w5_do_w1.csv");
            myObj.delete();
        }

    }
}
