package com.gminds.auth_server.model;


import jakarta.persistence.*;

@Entity
@Table(name="employee_user")
public class EmployeeUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String roles;

        public EmployeeUser() {
        }

        public EmployeeUser(Long id, String username, String password, String roles) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.roles = roles;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getRoles() {
                return roles;
        }

        public void setRoles(String roles) {
                this.roles = roles;
        }
}
