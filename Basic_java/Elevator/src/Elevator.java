public class Elevator {

    /*private int currentFloor = 1;
    This parameter is by the condition of the task,
    but you can not use it. The elevator is in its original
    position on the ground floor.
     */
    final int minFloor = -3;
    final int maxFloor = 26;
    private int currentFloor = getCurrentFloor();


    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;

    }

    public void moveUp(int floor) {
        if (floor > maxFloor) {
            System.out.println("Такого этажа не существует.");
            return;
        }
        int i;
        for (i = currentFloor; i < floor; i++) {
            System.out.println("Лифт двигается вверх.");
            System.out.println("Этаж: " + i);
        }
        currentFloor = i;


    }

    public void moveDown(int floor) {
        if (floor < minFloor) {
            System.out.println("Такого этажа не существует.");
            return;
        }
        int i;
        for (i = currentFloor; i > floor; i--) {
            System.out.println("Лифт двигается вниз.");
            System.out.println("Этаж: " + i);
        }
        currentFloor = i;


    }

    public void move(int floor) {
        System.out.println("Лифт находиться на " + currentFloor + " этаже.");
        moveUp(floor);
        moveDown(floor);
        setCurrentFloor(currentFloor);
    }


}

