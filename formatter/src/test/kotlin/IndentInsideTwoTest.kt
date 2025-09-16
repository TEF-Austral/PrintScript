class IndentInsideTwoTest {
}

//config:
//{
//  "indent-inside-if": 4
//}

//input:
//let something: boolean = true;
//if (something) {
//  if (something) {
//    println("Entered two ifs");
//  }
//}

//output:
//let something: boolean = true;
//if (something) {
//    if (something) {
//        println("Entered two ifs");
//    }
//}