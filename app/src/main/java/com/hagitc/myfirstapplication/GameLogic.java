package com.hagitc.myfirstapplication;

public class GameLogic
    //המחלקה  GameLogic מיועדת לניהול הלוגיקה של המשחק.
        // היא אחראית לביצוע פעולות שונות במשחק ולבדיקת תנאי הניצחון, הניצחון בשורות, בעמודות ובאלכסונים.
{
    private int arr [][] = new int [6][7];
    private int currentPlayer;//1 or -1
    private int counter;
    //the counter is counting the number of right moves של מהלכים תקינים.

    public int getCurrentPlayer()
    {
        return currentPlayer;
    }

    public GameLogic()
    {//מאפס את כל התאים של המערך הדו מימדי של המונים להיות 0.
        // בהמשך, כאשר המשתמש ילחץ על משבצת והיא תהיה ריקה, אני אעדכן את התא להיות -1
        //מאפסת את כל התאים של מערך המשבצות לערך 0. זהו הערך המשמש כסמל למשבצת ריקה.
        for(int i=0; i<arr.length; i++)
        {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = 0;
            }
        }
        this.currentPlayer = -1;
        this.counter = 0;
    }
    public int getCounter()
    {
        return this.counter;
    }
    public void setCounter(int counter1)
    {
        this.counter = counter1;
        //this function will update the counter after every click on the board.
    }

    public void switchPlayer ()
    {
        this.currentPlayer= this.currentPlayer * (-1);
    }

    public int checkLegalMove(int col)
    {
        boolean found = false;
        for(int i=arr.length-1; i>=0;i--)
        {
            if(arr[i][col] == 0)
            {
               return i;
            }
        }
        return  -1;

    }

    public int userClick(int touchedColumn)
    {
        //אני מקבלת כפרמטר את המספר העמודה שהמשתמש לחץ עליה.
        //אם העמודה לא מלאה, הפעולה תחזיר את המשבצת הראשונה שאפשר לצבוע אותה.
        //אם העמודה מלאה הפעולה תחזיר מינוס אחת.
        //הפעולה עוברת על השורות של הלוח מהשורה התחתונה לשורה העליונה עד שמוצאת משבצת ריקה (ערך 0).
        // כאשר היא מוצאת משבצת ריקה, היא מסמנת את המשבצת באותו סימון של מספר השחקן ומחזירה את מספר השורה בה נמצאה המשבצת הריקה.
        switchPlayer();
        int touchedRow = -1;
        boolean found = false;
        for(int i=arr.length-1; i>=0 &&!found;i--)
        {
            if(arr[i][touchedColumn] == 0)
            {
                arr[i][touchedColumn] = currentPlayer;
                found = true;
                touchedRow = i;
            }
        }
        if (found)
        {
            return touchedRow;
        }
        return touchedRow;
        //אם אין משבצות ריקות בעמודה שנבחרה (כלומר, העמודה מלאה), הפעולה מחזירה ערך -1,
        // מספר המסמן שהמהלך לא חוקי, והשחקן הנוכחי יבחר שוב.

    }

    public boolean checkForWin()
    {
        // Check for a win in rows, columns, and diagonals
        return checkRows() || checkColumns() || checkDiagonals();
    }

    private boolean checkRows() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (arr[i][j] != 0 &&
                        arr[i][j] == arr[i][j + 1] &&
                        arr[i][j] == arr[i][j + 2] &&
                        arr[i][j] == arr[i][j + 3]) {
                    return true; // Win in a row
                }
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (arr[i][j] != 0 &&
                        arr[i][j] == arr[i + 1][j] &&
                        arr[i][j] == arr[i + 2][j] &&
                        arr[i][j] == arr[i + 3][j]) {
                    return true; // Win in a column
                }
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (arr[i][j] != 0 &&
                        arr[i][j] == arr[i + 1][j + 1] &&
                        arr[i][j] == arr[i + 2][j + 2] &&
                        arr[i][j] == arr[i + 3][j + 3]) {
                    return true; // Win in a diagonal
                }
            }
        }

        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 3; j < 7; j++) {
                if (arr[i][j] != 0 &&
                        arr[i][j] == arr[i + 1][j - 1] &&
                        arr[i][j] == arr[i + 2][j - 2] &&
                        arr[i][j] == arr[i + 3][j - 3]) {
                    return true; // Win in the other diagonal
                }
            }
        }

        return false;
    }


    public boolean isBoardFull()
    {
        for(int i = 0; i< 6; i++)
        {
            for(int j = 0; j< 7; j++)
            {
                if (arr[i][j] == 0)
                {
                    return false; //board is not full
                }
            }
        }
        return true; //board is full
    }
}
