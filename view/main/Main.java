package view.main;

import view.login_register_view.Login_Register_View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static Login_Register_View login_register = new Login_Register_View();

    public static void main(String[] args) throws Exception {
        mainMenu();
    }

    public static void mainMenu() throws Exception {


        while (true) {
            System.out.print("""
                    =========================================
                    ========== [ 창고 관리 프로그램 ] ==========
                    ================ [ 메뉴 ] ================

                                   1. 사용자
                                   2. 관리자
                                   3. 종료

                    ==========================================
                    ==========================================
                    [메뉴 선택]: """);

            int choice = Integer.parseInt(br.readLine());

            switch (choice) {
                case 1 -> user();
                case 2 -> admin();
                case 3 -> {
                    return;
                }
            }
        }
    }

    public static void user() throws IOException{
       while (true) {
           System.out.print("""
                    =========================================
                    ========== [ 창고 관리 프로그램 ] ==========
                    ============= [ 사용자 메뉴 ] =============

                                   1. 로그인
                                   2. 회원가입
                                   3. 아이디 찾기
                                   4. 비밀번호 변경
                                   5. 종료

                    ==========================================
                    ==========================================
                    [메뉴 선택]: """);

           int choice = Integer.parseInt(br.readLine());

           switch (choice) {
               case 1 -> login_register.userLogin();
               case 2 -> login_register.userRegister();
               case 3 -> login_register.findUserID();
               case 4 -> login_register.changeUserPW();
               case 5 -> {
                   return;
               }

           }
       }
    }

    public static void admin() throws Exception {
        while (true) {
            System.out.print("""
                    =========================================
                    ========== [ 창고 관리 프로그램 ] ==========
                    ============= [ 관리자 메뉴 ] =============

                                   1. 로그인
                                   2. 회원가입
                                   3. 아이디 찾기
                                   4. 비밀번호 변경
                                   5. 종료

                    ==========================================
                    ==========================================
                    [메뉴 선택]: """);

            int choice = Integer.parseInt(br.readLine());

            switch (choice) {
                case 1 -> login_register.adminLogin();
                case 2 -> login_register.adminRegister();
                case 3 -> login_register.findAdminID();
                case 4 -> login_register.changeAdminPW();
                case 5 -> {return ;}

            }
        }
        }
}