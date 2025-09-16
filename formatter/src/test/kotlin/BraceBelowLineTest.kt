class BraceBelowLineTest {
}

//config:
//{
//  "if-brace-below-line": true
//}

//input:
//let something: boolean = true;
//if (something) {
//  println("Entered if");
//}

//output:
//let something: boolean = true;
//if (something)
//{
//  println("Entered if");
//}