# Handler-Memory-Leak
Handler内存泄漏的处理方法

过去 Android 使用 Handler 进行线程间通信来更新 UI ，但在 Android Studio 2.3.3 之后使用传统的写法 IDE 会提示：

    ”This Handler class should be static or leaks might occur.“


传统写法如下：

    public class MainActivity extends Activity {
                                                               
        private final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
              // TODO
            }
          }
          
    }


Android Framework 的工程师 Romain Guy 在 Google 论坛上做出过解释：

    I wrote that debugging code because of a couple of memory leaks I 
    found in the Android codebase. Like you said, a Message has a 
    reference to the Handler which, when it's inner and non-static, has a 
    reference to the outer this (an Activity for instance.) If the Message 
    lives in the queue for a long time, which happens fairly easily when 
    posting a delayed message for instance, you keep a reference to the 
    Activity and "leak" all the views and resources. It gets even worse 
    when you obtain a Message and don't post it right away but keep it 
    somewhere (for instance in a static structure) for later use. 

并且给出了他的建议写法：

    class OuterClass {
                                     
      class InnerClass {
        private final WeakReference<OuterClass> mTarget;
                                     
        InnerClass(OuterClass target) {
               mTarget = new WeakReference<OuterClass>(target);
        }
                                     
        void doSomething() {
               OuterClass target = mTarget.get();
               if (target != null) {
                    target.do();    
               }
         }
    }
