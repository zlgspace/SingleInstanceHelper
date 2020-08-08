# SingleInstanceHelper
一般情况在代码中创建的单例，在引用时非常麻烦，必须先getInstance然后再调用对应的函数执行操作，所以为了简化这种调用方式，这里采用APT,自动生成对应类的静态单例类，直接通过类名.函数的方式调用

* 使用

 只要生成类取的名称一致，即可将多个Entity合并成一个单例类
 但有一点需要注意，每个了类中的public函数必须不一致，否则生成类报错
 
@SingleInstance("SingleAPPState")  
public class APPState  

@SingleInstance参数选填，默认以‘SG_标记类名’为名生成单例类
