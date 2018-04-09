# 基于lance的代码修改的换肤库（主要改动为修改细节代码，纠正少许逻辑错误，添加对非xml生成对布局换肤支持） 

 1。支持src，background，textColor，drawableLeft，drawableTop，drawableRight，drawableBottom，字体skinTypeface（必须定义string配置字体路径）
 
 2。对于自定义view的其它属性或者对于viewgroup的子类的换肤，请实现SkinViewSupport接口自行设置皮肤
 
 3。本库同时支持xml和new view两种布局生成方式。对于第二种通过设置tag进行标示需要换肤的属性如：
 ```tv.setTag(R.id.tag_textColor,R.color.colorAccent);```
 
 4。切记：当使用换肤库时别用资源混淆方案了。
 
 
#使用
maven { url 'https://jitpack.io' }

compile 'com.github.wangdanlizhiyun:skin:1.0.1'
 
 
 在application里初始化
    ```
        SkinManager.init(this);
    ```
    
  护肤时，下载皮肤后设置路径即可
  ```SkinManager.getInstance().loadSkin(skin.path);```
  
  
  #TODO：当考虑到基于自定义view的兼容webp的库也是setfactory或者setfactory2并且重新生成drawable或者statelistdrawable时，为陷入了沉思。。。
  
  
  #效果图：
  请使用命令查看
         dryrun https://github.com/wangdanlizhiyun/skin.git
  
  