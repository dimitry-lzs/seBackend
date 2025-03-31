import { IsInt, IsDefined, IsString, IsOptional } from "class-validator";
import "./";

export class User {
    @IsDefined()
    @IsInt()
    id!: number;

    @IsDefined()
    @IsString()
    email!: string;

    @IsOptional()
    @IsString()
    fullname?: string;

    @IsDefined()
    @IsString()
    password!: string;
}
