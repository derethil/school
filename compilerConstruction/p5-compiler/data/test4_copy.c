// 0 byes

void main() {
  int a; // -4
  int b; // -8

  println("This program prints 7 7 7");
  a = 3;
  b = 2;
  { // New symbol table, parent is using 8 bytes total
    int a;
    a = 5; // -4
    // b is 0 because not found + parent is using 8 bytes total
    println(a+b);
    {
        int b;
        b = 9; // -4
        a = -2; // -8
        println(a+b);
    }
    b = 4;
  }
  println(a+b);
}
