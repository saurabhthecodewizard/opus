import { createSlice } from "@reduxjs/toolkit";
import { JwtResponse } from "../../../openapi";
import { loginAction } from "../actions/authAsyncThunkActions.action";


const initialState: JwtResponse = {
    token: "",
    username: ""
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(loginAction.pending, (state) => {
                state.token = '';
            })
            .addCase(loginAction.fulfilled, (state, action) => {
                state = action.payload;
                localStorage.setItem('token', state.token ?? '');
            })
            .addCase(loginAction.rejected, (state, action) => {
                state.token = '';
            });
    },
});

export default authSlice.reducer;