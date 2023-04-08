package me.sean.sudoku.model;

public class Solver {
    public static Config solve(Config config) {
        System.out.println(config);
        if(config.isWon()) {
            return config;
        }
        for(Config child : config.generateChildren()) {
            if(child.isValid()) {
                 Config solved = solve(child);
                 if(solved != null) {
                     return solved;
                 }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java Solver <filepath>");
            System.exit(-1);
        }
        Model model = new Model(args[0], null);
        solve(model).toString();
    }

}
