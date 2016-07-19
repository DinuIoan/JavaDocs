package ro.teamnet.zth.web;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import ro.teamnet.zth.api.annotations.Id;
import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.api.annotations.MyRequestParam;
import ro.teamnet.zth.appl.controller.DepartmentController;
import ro.teamnet.zth.appl.controller.EmployeeController;
import ro.teamnet.zth.appl.domain.Employee;
import ro.teamnet.zth.fmk.AnnotationScanUtils;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by user on 7/14/2016.
 */
public class DispatcherServlet extends HttpServlet {
    //rol de registru
    //key: url , val : informatii despre metoda care va provesa acest url
    HashMap<String,MethodAttributes> allowedMethods = new HashMap<String,MethodAttributes>();

    @Override
    public void init() throws ServletException {
        try {
            //cautare clase din pachet
            Iterable<Class> classes = AnnotationScanUtils.getClasses("ro.teamnet.zth.appl.controller");
            for (Class controller : classes) {
                if(controller.isAnnotationPresent(MyController.class)){
                    MyController myController = (MyController) controller.getAnnotation(MyController.class);
                    String controllerUrlPath = myController.urlPath();
                    Method[] controllerMethods = controller.getMethods();
                    for(Method controllerMethod: controllerMethods){
                        if(controllerMethod.isAnnotationPresent(MyRequestMethod.class)){
                            MyRequestMethod myRequestMethod = controllerMethod.getAnnotation(MyRequestMethod.class);
                            String methodUrlPath = myRequestMethod.urlPath();
                            //construiesc cheia pt hashmap
                            String urlPath = controllerUrlPath + methodUrlPath;

                            //construiesc valoarea
                            MethodAttributes methodAttributes = new MethodAttributes();
                            methodAttributes.setControllerClass(controller.getName());
                            methodAttributes.setMethodType(myRequestMethod.methodType());
                            methodAttributes.setMethodName(controllerMethod.getName());
                            methodAttributes.setParameterTypes(controllerMethod.getParameterTypes());

                            //adaug in registru
                            allowedMethods.put(urlPath,methodAttributes);
                        }
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("GET",req,resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("POST",req,resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply("DELETE",req,resp);
    }

    protected void dispatchReply(String methode, HttpServletRequest req, HttpServletResponse resp)  {
        Object r = new Object();
        try{
            r = dispatch(req,resp);

        }catch(Exception e){
            sendExceptionError(e,req,resp);
        }

        try {
            reply(r,req,resp);
        } catch (Exception e) {
            sendExceptionError(e,req,resp);
        }


    }

    protected Object dispatch(HttpServletRequest req, HttpServletResponse resp)  {
        String path = req.getPathInfo();

        MethodAttributes methodAttributes = allowedMethods.get(path);

        if(methodAttributes == null){
            return "NULL";
        }
        String controllerName = methodAttributes.getControllerClass();
            try {
                Class<?> controllerClass = Class.forName(controllerName);
                Object controllerInstance = controllerClass.newInstance();
                Method method = controllerClass
                    .getMethod(methodAttributes.getMethodName(),methodAttributes.getParameterTypes());
                Parameter[] methodParameters = method.getParameters();
                Object result;
                List<Object> parameterValue = new ArrayList<>();
                for(Parameter p: methodParameters){
                    if(p.isAnnotationPresent(MyRequestParam.class)){
                        MyRequestParam anotation = p.getAnnotation(MyRequestParam.class);
                        String name = anotation.name();
                        String requestParamValue = req.getParameter(name);
                        Class<?> type = p.getType();
                        Object requestParamObject = requestParamValue;
                        if(!type.equals(String.class)){
                             requestParamObject = new ObjectMapper().readValue(requestParamValue, type);
                        }
                        parameterValue.add(requestParamObject);

                    }
                }

            return method.invoke(controllerInstance,parameterValue.toArray());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return "Hello";
    }

    protected void reply(Object r, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String res = mapper.writeValueAsString(r);
        out.printf(res);
    }

    protected void sendExceptionError(Exception e,HttpServletRequest req , HttpServletResponse resp){

    }
}
