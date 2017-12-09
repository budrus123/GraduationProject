public class ScoringSystem {

    public static double score(int a[][]){
        double countScore = 0;
        double countEmptyDays = 0;
        double score = 0;
        double percentageOfConsecutiveDays = maxNumberOfConsecutiveDaysThatHaveExams(a);


        int firstExam = firstExam(a);
        int lastExam = lastExam(a);
        for (int i = firstExam+1; i <= lastExam; i++) {
            if (numberOfExams(a[i]) == 0){
                if(countScore < 1){
                    countScore++;
                    countEmptyDays++;
                }
                else{
                    if (percentageOfConsecutiveDays < 0.5){
                        countScore+=1/(countEmptyDays+1);
                    }
                    countEmptyDays++;
                }
            }
            else if(numberOfExams(a[i]) == 1){
                score+=countScore;
                score+=scoreOfTwoConsecutiveDays(a[i-1],a[i]);
//                System.out.println(countScore + scoreOfTwoConsecutiveDays(a[i-1],a[i]));
                countScore = 0;
                countEmptyDays = 0;
            }
            else if(numberOfExams(a[i]) == 2){
                score+=countScore/2.0;
                countScore = 0;
                countEmptyDays = 0;
            }

        }

        return score;
    }


    public static void printArray(int a[][]){

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static int numberOfExams(int a[]){
        return a[0] + a[1] + a[2];
    }

    public static int firstExam(int a[][]){
        for (int i = 0; i < a.length; i++) {
            if(numberOfExams(a[i]) > 0){
                return i;
            }
        }
        return -1;
    }


    public static int lastExam(int a[][]){
        int lastExam = -1;
        for (int i = 0; i < a.length; i++) {
            if(numberOfExams(a[i]) > 0){
                lastExam = i;
            }
        }
        return lastExam;
    }


    public static double scoreOfTwoConsecutiveDays(int pre[],int current[]){
        int lastInPre = -1;
        int firstInCurrent = -1;
        for (int i = 0; i < pre.length; i++) {
            if (pre[i] == 1){
                lastInPre = i;
            }
        }

        for (int i = 0; i < current.length; i++) {
            if (current[i] == 1){
                firstInCurrent = i + 2;
                break;
            }
        }

        // Best case: score : 0.7
        // 1 0 0   -- pre
        // 0 0 1   -- last

        if (numberOfExams(pre) < 1 && numberOfExams(current) < 1){
            return 0;
        }

        if (numberOfExams(pre) < 1){

            return (0.7 / 4) * (firstInCurrent-2);
        }

        return (0.7 / 4) * Math.abs(firstInCurrent - lastInPre);
    }

    public static double maxNumberOfConsecutiveDaysThatHaveExams(int a[][]){

        double numberOfAllExams = 0;
        double max = 0;
        double counter = 0;
        for (int i = 0; i < a.length; i++) {
            numberOfAllExams+=numberOfExams(a[i]);
            if (numberOfExams(a[i]) > 0){
                counter+=numberOfExams(a[i]);
            }
            else{
                if (counter > max){
                    max = counter;
                }
                counter = 0;
            }
        }
        if (counter > max){
            max = counter;
        }
        return max/numberOfAllExams;
    }


}
