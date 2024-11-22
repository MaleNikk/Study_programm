package testing;

import org.jetbrains.annotations.NotNull;
public class Test {
    public Test(){
        System.out.println("Test initialize.");
    }
    public boolean checkHyperText(@NotNull String data){
        return data.contains("http://") || data.contains("https://")                ;
    }
    public boolean checkChar(@NotNull String data, String requiredChar){ return data.contains(requiredChar);}
}
