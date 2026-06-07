package cupcake.nikky.imperium.tower.level;

import lombok.Getter;

public final class LevelStorage {

    @Getter
    private static final LevelStorage instance = new LevelStorage();

    private final int easyKey = 150;
    private final int mediumKey = 225;
    private final int hardKey = 300;

    private final int[][] basicStorage = new int[][] {
            {1, easyKey * 3},
            {2, easyKey * 2 * 8},
            {3, easyKey * 3 * 6},
            {4, easyKey * 4 * 6},
            {5, easyKey * 5 * 7},
            {6, easyKey * 6 * 7},
            {7, easyKey * 7 * 8},
            {8, easyKey * 8 * 8},
            {9, easyKey * 9 * 8},
            {10, easyKey * 10 * 9},
            {11, easyKey * 11 * 9},
            {12, easyKey * 12 * 9},
            {13, easyKey * 13 * 10},
            {14, easyKey * 14 * 10},
            {15, easyKey * 15 * 10},
            {16, easyKey * 16 * 12},
            {17, easyKey * 17 * 12},
            {18, easyKey * 18 * 12},
            {19, easyKey * 19 * 13},
            {20, easyKey * 20 * 13},
            {21, easyKey * 21 * 13},
            {22, easyKey * 22 * 14},
            {23, easyKey * 23 * 14},
            {24, easyKey * 24 * 14},
            {25, easyKey * 25 * 16},
            {26, easyKey * 26 * 16},
            {27, easyKey * 27 * 16},
            {28, easyKey * 28 * 17},
            {29, easyKey * 29 * 17},
            {30, easyKey * 30 * 17},
            {31, easyKey * 31 * 18},
            {32, easyKey * 32 * 18},
            {33, easyKey * 33 * 18},
            {34, easyKey * 34 * 19},
            {35, easyKey * 35 * 19},
            {36, easyKey * 36 * 19},
            {37, easyKey * 37 * 20},
            {38, easyKey * 38 * 20},
            {39, easyKey * 39 * 20},
            {40, easyKey * 40 * 21},
            {41, easyKey * 41 * 21},
            {42, easyKey * 42 * 21},
            {43, easyKey * 43 * 22},
            {44, easyKey * 44 * 22},
            {45, easyKey * 45 * 22},
            {46, easyKey * 46 * 23},
            {47, easyKey * 47 * 23},
            {48, easyKey * 48 * 23},
            {49, easyKey * 49 * 24},
            {50, easyKey * 50 * 24},
            {51, easyKey * 51 * 24},
            {52, easyKey * 52 * 25},
            {53, easyKey * 53 * 25},
            {54, easyKey * 54 * 25},
            {55, easyKey * 55 * 26},
            {56, easyKey * 56 * 26},
            {57, easyKey * 57 * 26},
            {58, easyKey * 58 * 27},
            {59, easyKey * 59 * 27},
            {60, easyKey * 60 * 27},
            {61, easyKey * 61 * 30},
            {62, mediumKey * 62 * 40},
            {63, mediumKey * 63 * 60},
            {64, mediumKey * 64 * 70},
            {65, mediumKey * 65 * 90},
            {66, mediumKey * 66 * 100},
            {67, mediumKey * 67 * 110},
            {68, mediumKey * 68 * 120},
            {69, mediumKey * 69 * 130},
            {70, mediumKey * 70 * 140},
            {71, mediumKey * 71 * 150},
            {72, mediumKey * 72 * 160},
            {73, mediumKey * 73 * 170},
            {74, mediumKey * 74 * 180},
            {75, mediumKey * 75 * 190},
            {76, mediumKey * 76 * 200},
            {77, hardKey * 77 * 250},
            {78, hardKey * 78 * 300},
            {79, hardKey * 79 * 350},
            {80, hardKey * 80 * 400},
            {81, hardKey * 81 * 500},
            {82, hardKey * 82 * 600},
            {83, hardKey * 83 * 700},
            {84, hardKey * 84 * 800},
            {85, hardKey * 85 * 900},
            {86, hardKey * 86 * 1000},
            {87, hardKey * 87 * 2000},
            {88, hardKey * 88 * 3000},
            {89, hardKey * 89 * 4000},
            {90, hardKey * 90 * 5000},
            {91, hardKey * 90 * 6000},
            {92, hardKey * 90 * 7000},
            {93, hardKey * 90 * 8000},
            {94, hardKey * 90 * 9000},
            {95, hardKey * 90 * 10000},
            {96, hardKey * 90 * 11000},
            {97, hardKey * 90 * 12000},
            {98, hardKey * 90 * 13000},
            {99, hardKey * 90 * 14000},
            {100, hardKey * 90 * 15000},
    };

    @Getter private final int armorLevel = 100;
    @Getter private final int breakThroughLevel = 61;
    @Getter private final int firstStageLevel = 15;
    @Getter private final int secondStageLevel = 30;
    @Getter private final int thirdStageLevel = 50;
    @Getter private final int startShardEdgeLevel = 61;

    public double getTopExpKey() {
        return basicStorage[basicStorage.length - 1][1];
    }

    public int getMaxBasicLevel() {
        return basicStorage.length;
    }

    public int getBasicRequirementToNextLevel(final int level) {
        return basicStorage.length <= level ? -1 : basicStorage[level - 1][1];
    }

}
