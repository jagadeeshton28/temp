import axios from 'axios';
import toastr from 'toastr';
import validator from 'validator';
/*
 * action creators for get product list
 */
const baseURL = "http://localhost:8965/api/v1/";
//const baseURL = "http://ec2-3-23-63-5.us-east-2.compute.amazonaws.com:8965/";

export function getProducts(userType, searchEmail) {
  let url;

  if (validator.isEmail(searchEmail)) {
    if (userType === 'seller') {
      url = baseURL + "seller/products/" + searchEmail

      return function (dispatch, getState) {
        //return dispatch({ type: 'GET_PRODUCTS', productList: data.products });

        axios.get(url, {
          headers: {
            'Content-Type': 'application/json',
          }
        })
          .then(function (res) {
            if (res.data.response.statusCode === 200) {
              return dispatch({ type: 'GET_PRODUCTS', productList: res.data.products });
            } else {
              toastr.error(res.data.response.msg);
            }

          }).catch(err => console.log(err));
      }
    } else if (userType === 'buyer') {
      url = baseURL + "seller/products"

      return function (dispatch, getState) {
        //return dispatch({ type: 'GET_PRODUCTS', productList: data.products });

        axios.get(url, {
          headers: {
            'Content-Type': 'application/json',
          }
        })
          .then(function (res) {
            if (res.data.response.statusCode === 200) {
              return dispatch({ type: 'GET_PRODUCTS', productList: res.data.products });
            } else {
              toastr.error(res.data.response.msg);
            }

          }).catch(err => console.log(err));
      }
    }
  }


  
}

/*
 * action creators for Add Product
 */
export function addProduct(data) {
  // http://192.168.1.100:8965/api/v1/seller/add-product

  
  let url = baseURL + "seller/add-product"

  return function (dispatch, getState) {
    //return dispatch({ type: 'ADD_PRODUCT', data: data.product });
    axios.post(url, data, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(function (res) {
        console.log('res=>', res);
        if (res.data.response.statusCode === 200) {
          return dispatch({ type: 'ADD_PRODUCT', data: res.data.product });
        } else {
          toastr.error(res.data.response.msg);
        }
      }).catch(err => console.log(err));
  }
}

/*
 * action creators for edit Product
 */
export function editProduct(data) {

  return function (dispatch, getState) {
    return dispatch({ type: 'UPDATE_PRODUCT', data: data });

  }
}


/*
 * action creators for delete Product
 */
export function deleteProduct(productId) {
  // http://192.168.1.100:8965/api/v1/seller/delete/{productId}

  let url = baseURL + "seller/delete/" + productId

  return function (dispatch, getState) {

    //return dispatch({ type: 'DELETE_PRODUCT', productId: productId });

    axios.put(url)
      .then(function (res) {
        if (res.data.response.statusCode === 200) {
          return dispatch({ type: 'DELETE_PRODUCT', productId: productId });
        } else {
          toastr.error(res.data.response.msg);
        }
      }).catch(err => console.log(err));
  }
}

/*
 * action creators for bid Product
 */
export function bidProduct(data) {
  // http://192.168.1.100:8965/api/v1/buyer/place-bid

  let url = baseURL + "buyer/place-bid"

  return function (dispatch, getState) {
    //return dispatch({ type: 'BID_PRODUCT', data: data });

    axios.put(url, data, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(function (res) {
        if (res.data.response.statusCode === 200) {
          return dispatch({ type: 'BID_PRODUCT', data: res.data.buyer });
        } else {
          toastr.error(res.data.response.msg);
        }
      }).catch(err => console.log(err));
  }
}

/*
 * action creators for update bid
 */
export function bidUpdate(data) {
  
  // Ex- http://192.168.1.100:8965/api/v1/bid/4444/bbbb2236@bbb2236.com/2000

  let buyerEmailId = localStorage.getItem('searchEmail');
  let url = baseURL + "bid/" + data.productId + "/" + buyerEmailId + "/" + data.bidAmount

  return function (dispatch, getState) {
    //return dispatch({ type: 'BID_UPDATE', data: data });

    axios.put(url)
      .then(function (res) {
        if (res.data.response.statusCode === 200) {
          return dispatch({ type: 'BID_PRODUCT', data: res.data.buyer });
        } else {
          toastr.error(res.data.response.msg);
        }
      }).catch(err => console.log(err));
  }
}

/*
 * action creators for update bid
 */
export function showBids(productId) {
  // http://192.168.1.100:8965/api/v1/bid/{productId}/{buyerEmailId}/{newBidAmount}

  // Ex- http://192.168.1.100:8965/api/v1/bid/4444/bbbb2236@bbb2236.com/2000

  let url = baseURL + "seller/show-bids/" + productId
  // let data = [{
  //   "firstName": "rrrrr",
  //   "lastName": "uuuuuu",
  //   "address": "aaaaa",
  //   "city": "hydera",
  //   "state": "tss",
  //   "pin": "string",
  //   "phone": "3455689023",
  //   "email": "bbbb2236@bbb2236.com",
  //   "productId": "6",
  //   "bidAmount": "1111",
  //   "productName": "abcdef"
  // }]
  return function (dispatch, getState) {
    //return dispatch({ type: 'SHOW_BIDS', bidList: data });

    axios.get(url)
      .then(function (res) {
        if (res.data.response.statusCode === 200) {
          return dispatch({ type: 'SHOW_BIDS', bidList: res.data.buyers });
        } else {
          toastr.error(res.data.response.msg);
        }
      }).catch(err => console.log(err));
  }
}