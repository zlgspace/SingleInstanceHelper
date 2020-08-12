package com.zlgspace.singleinstancehelper;

import com.google.auto.service.AutoService;
import com.zlgspace.singleinstancehelper.annotation.SingleInstance;
import com.zlgspace.singleinstancehelper.build.BuildClass;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class SingleInstanceProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private Filer mFiler;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add(SingleInstance.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(SingleInstance.class);
        Map<String,BuildClass> buildClasses = new HashMap<String,BuildClass>();
        for (Element e : elements) {
               analysisAnnotated((TypeElement) e,buildClasses);
        }
        for(String key:buildClasses.keySet()){
//            print("importPkgBuilder = \n" + buildClasses.get(key).toString());
            createClass(buildClasses.get(key));
        }


        return false;
    }

    /**
     * 生成java文件
     *
     * @param classElement 注解
     */
    private BuildClass analysisAnnotated(TypeElement classElement,  Map<String,BuildClass> buildClasses) {
        SingleInstance annotation = classElement.getAnnotation(SingleInstance.class);
        String clazzSimpleName = classElement.getSimpleName().toString();
        String clazzFullName = classElement.getQualifiedName().toString();
        String pkgName = elementUtils.getPackageOf(classElement).toString();

        String classSimpleName = annotation.value();
        if (classSimpleName == null || classSimpleName.trim().isEmpty()) {
            classSimpleName = "SG_" + clazzSimpleName;
        } else if (classSimpleName.equals(clazzSimpleName)) {
            classSimpleName = "SG_" + clazzSimpleName;
            print("Class already exist ,change name:" + clazzSimpleName);
        }

        String instanceName = clazzSimpleName.toUpperCase()+"_INSTANCE";
        BuildClass buildClass = null;
        if(!buildClasses.containsKey(classSimpleName)) {

            buildClass = new BuildClass();
            buildClass.setPackage(pkgName);
            buildClass.setName(classSimpleName);
            buildClass.setClzModifiers("public final class");
            buildClass.appendImport("package " + pkgName + ";\n");

            buildClasses.put(classSimpleName,buildClass);
        }else{
            buildClass = buildClasses.get(classSimpleName);
        }

        buildClass.appendField("private static final " + clazzFullName +" "+ instanceName +" = new " + clazzFullName + "();\n");
        buildClass.appendMethod("public synchronized static " + clazzFullName +" get"+clazzSimpleName+ "Instance(){\n");
        buildClass.appendMethod("return "+instanceName+";\n");
        buildClass.appendMethod("}\n");

        List<? extends Element> fieldList = elementUtils.getAllMembers(classElement);
        for (Element e : fieldList) {
            if (e.getKind() == ElementKind.METHOD) {
                Set<Modifier> modifier = e.getModifiers();
                if (!modifier.contains(Modifier.PUBLIC)) {
                    continue;
                }

                if (isObjectMethod(e)) {//过滤Object 函数
                    continue;
                }

                String methodSimpleName = e.getSimpleName().toString();

                ExecutableElement methodElement = ((ExecutableElement) e);
                String returnType = methodElement.getReturnType().toString();
                buildClass.appendMethod("public synchronized static " + returnType + " " + methodSimpleName + "(");

                String parameterStr = "";

                String parameterValueStr = "";

                List<? extends VariableElement> parameters = methodElement.getParameters();
                for (VariableElement element : parameters) {
                    String paramType = getParamType(element);
//                    print("paramPackage = "+paramType);
                    String paramName = element.getSimpleName().toString();
//                    print("paramName = "+paramName);

                    if (!parameterStr.isEmpty())
                        parameterStr = parameterStr + " , ";
                    parameterStr = parameterStr + paramType + " " + paramName;

                    if (!parameterValueStr.isEmpty())
                        parameterValueStr = parameterValueStr + ",";
                    parameterValueStr = parameterValueStr + paramName;
                }

                buildClass.appendMethod(parameterStr + "){\n");
                if (methodElement.getReturnType().getKind() == TypeKind.VOID)
                    buildClass.appendMethod(instanceName+"." + methodSimpleName + "(" + parameterValueStr + ");\n");
                else
                    buildClass.appendMethod("return "+instanceName+"."+methodSimpleName + "(" + parameterValueStr + ");\n");
                buildClass.appendMethod("}\n");
            }
        }

        return buildClass;
}

    private boolean isObjectMethod(Element e){
        Method[] methods = Object.class.getDeclaredMethods();
        String methodSimpleName = e.getSimpleName().toString();
        for(Method m:methods){
            if(m.getName().equals(methodSimpleName)){
                    return true;
            }
        }
        return false;
    }

    private String getParamType(VariableElement element){
        return element.asType().toString();
    }

    private void createClass(BuildClass buildClass){
        try { // write the file
            JavaFileObject source = mFiler.createSourceFile(buildClass.getPackage() + "." + buildClass.getName());
            Writer writer = source.openWriter();
            writer.write(buildClass.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }


    private void print(String msg){
        System.out.println("MessageParserProcessor:"+msg);
    }
}
