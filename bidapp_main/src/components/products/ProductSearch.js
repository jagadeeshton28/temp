import React, { useState } from 'react';
import './ProductSearch.css';
import validator from 'validator';
export default function ProductSearch({handleSearchChange, getValues}) {
const [emailError, setEmailError] = useState('')
  const validateEmail = (e) => {
    var email = e.target.value
      
    
    if (validator.isEmail(email)) {
    setEmailError('')
    } else {
     // e.target.value = '';
      setEmailError('Enter valid Email!')
    }
  }
    const formStyle = {
        marginTop: 12,
        marginRight: 20,
        marginLeft: 20,
        borderBottom: "1px solid #ccc"
    };
    const inputStyle = {
        paddingTop: 5,
        paddingLeft: 10,
        height: 45,
        width: 400,
        border: '1px solid #ccc',
        backgroundColor: 'white'
    };


    return (
        <form className="product-search-form" style={formStyle}>
            <pre>
            <input placeholder="Enter Email Id"
                style={inputStyle}
                name="searchEmail"
                onChange={(e) => {validateEmail(e), getValues(e) }}
                />
                  <span style={{
          fontWeight: 'bold',
          color: 'red',
                }}>{emailError}</span>
            </pre>
            
            <input type="radio" name="searchkeybtn" value="seller" onChange={(e) => { handleSearchChange(e) }} />Seller
            <input type="radio" name="searchkeybtn" value="buyer" onChange={(e) => { handleSearchChange(e) }} />Buyer
            <hr/>
        </form>
        
    )
}