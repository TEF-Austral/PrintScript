class DeclarationSpacingBeforeColonTest {
}

//config:
//{
//  "enforce-spacing-before-colon-in-declaration": true
//}

//input:
//let something:string = "a really cool thing";
//let another_thing :string = "another really cool thing";
//let twice_thing : string = "another really cool thing twice";
//let third_thing: string="another really cool thing three times";

//output:
//let something :string = "a really cool thing";
//let another_thing :string = "another really cool thing";
//let twice_thing : string = "another really cool thing twice";
//let third_thing : string="another really cool thing three times";