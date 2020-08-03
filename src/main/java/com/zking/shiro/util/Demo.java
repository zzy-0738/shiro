package com.zking.shiro.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        //1.创建IIniSecurityManagerFactory读取加载ini配置文件
        IniSecurityManagerFactory factory=
                new IniSecurityManagerFactory("classpath:shiro-auth.ini");

        //2.创建securityManager安全管理器
        SecurityManager securityManager = factory.getInstance();

        //3.将securityManager安全管理器交由securityUtils关联
        SecurityUtils.setSecurityManager(securityManager);

        //4.创建Subject
        //Subject：主体、抽象的，在这里我们使用账号密码方式
        Subject subject = SecurityUtils.getSubject();

        //5.创建身份校验的token令牌
        //token：身份和凭据
        //IniSecurityManagerFactory：密码错误
        //UnknowAccountException:账号错误
        UsernamePasswordToken token=new UsernamePasswordToken("zs","123");

        try {
            //6.进行身份验证
            subject.login(token);
            System.out.println("身份验证通过！");
        }catch (Exception e){
            e.printStackTrace();
        }

        if (subject.hasRole("role1")) {
            System.out.println("角色验证通过！");
        } else {
            System.out.println("角色验证失败！");
        }

        try {
            subject.checkRole("role1");
            List<String> lst=new ArrayList<>();
            lst.add("role1");
            lst.add("role2");
            boolean[] roles = subject.hasRoles(lst);
            System.out.println(Arrays.toString(roles));
            boolean b = subject.hasAllRoles(lst);
            System.out.println(b);

        } catch (AuthorizationException e) {
            e.printStackTrace();
        }

        if (subject.isPermitted("user:select")) {
            System.out.println("权限验证通过！");
        } else {
            System.out.println("权限验证失败！");
        }

        try {
            subject.checkRole("user:delete");
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }


        //7.退出
        subject.logout();
    }
}
