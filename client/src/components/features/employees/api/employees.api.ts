import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { EmployeeApi, EmployeeDetailsDTO } from "../../../../openapi";
import { BASE_PATH } from "../../../../openapi/base";


const employees = new EmployeeApi();

export const employeeApi = createApi({
    reducerPath: 'employeeApi',
    baseQuery: fetchBaseQuery({ baseUrl: BASE_PATH }),
    endpoints: builder => ({
        getAllEmployees: builder.query<EmployeeDetailsDTO[], void>({
            queryFn: async () => {
                const response = await employees.getEmployees();
                return response;
            }
        })
    })
})

export const { useGetAllEmployeesQuery } = employeeApi;