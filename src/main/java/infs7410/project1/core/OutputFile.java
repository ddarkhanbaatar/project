package infs7410.project1.core;

public class OutputFile {

    private String fileName;
    private String method;
    private String year;
    private String type;

    public OutputFile(String f, String m, String y, String t) {
        this.fileName=f;
        this.method=m;
        this.year=y;
        this.type=t;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
