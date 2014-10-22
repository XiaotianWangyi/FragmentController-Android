FragmentController-Android
==========================

一个基于android Framwork的简单的Fragment的管理器（基于v4包）

具备切换动画，SINGLE_TASK,SINGER_TOP,NORMAL三个fragment堆管理模式，设置临时动画可以使下一次跳转具备临时动画，不设置动画则无转场动画，随便写的，应付基本的fragment的app管理没问题。

基本用法：
创建一个FragmentActivity的子类对象，接着new出FragmentController对象，设置相应的参数，在Activity中调用相应的方法，注意，需要复写Activity的onBackPressed方法，调用FragmentController的backPressed方法，堆栈管理FragmentManager会搞定。
更多功能欢迎补充。

