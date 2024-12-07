import React, { useState } from "react";
const Login = () => {

const [formData,setFormData] = useState({
    username: "",
    password: "",
  });

const handleChange = (e) => {
 setFormData({ ...formData, [e.target.name]: e.target.value });
  };

 return (
    <div className="login-container">
      <div className="login-form">
        <h1>Login</h1>

         <div className="form-group">
            <input type="text" name="username" placeholder="Username" value={formData.username}
                    onChange={handleChange} />
            <input type="password" name="password" placeholder="Password" value={formData.password}
                    onChange={handleChange} />
         </div>
      </div>
    </div>
 );
};


export default Login;