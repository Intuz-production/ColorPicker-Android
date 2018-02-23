<h1>Introduction</h1>

INTUZ is presenting an interesting Color Picker to integrate inside your Android based application. Please follow the below steps to integrate this control in your project.

<br>
<h1>Features</h1>

- Easy & fast ways to pick colors.
- You can able to change color alpha component.
- Can open in dialog or activity.
- Fully customizable layout.


<br>
<img src="Screenshots/colorpicker.gif" width=500 alt="Screenshots/colorpicker.png">

<h1>Getting Started</h1>

> To open Color Picker in activity

```
    private static final int REQUEST_COLOR_PICKER = 1;
    
    Intent i = new Intent(MainActivity.this, ColorPickerActivity.class);
    startActivityForResult(i, REQUEST_COLOR_PICKER);
    
    // Get result in onActivityResult as:
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //handle color picker activity result
    if (resultCode == RESULT_OK && requestCode == REQUEST_COLOR_PICKER) {
    
        selectedColor = data.getStringExtra("selectedColor");
     
    }
}
    

```
Note: You can also open Color Picker in a dialog.


<h1>Bugs and Feedback</h1>

For bugs, questions and discussions please use the Github Issues.

<br>
<h1>License</h1>

Copyright (c) 2018 Intuz Solutions Pvt Ltd.
<br><br>
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
<br><br>
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

<h1></h1>
<a href="http://www.intuz.com">
<img src="Screenshots/logo.jpg">
</a>