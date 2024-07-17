import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { AdminModule } from './modules/admin/admin.module';
import { HomeComponent } from './home/layouts/home.component';
import { Error404Component } from './home/views/error404/error404.component';
import { SignInComponent } from './home/views/sign-in/sign-in.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ForgotPasswordComponent } from './home/views/forgot-password/forgot-password.component';
import { HttpClientModule } from '@angular/common/http';
import { ResetPasswordComponent } from './home/views/reset-password/reset-password.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { VerifyAccoutComponent } from './home/views/verify-accout/verify-accout.component';
import { SignUpComponent } from './home/views/sign-up/sign-up.component';
import { CodeInputModule } from 'angular-code-input';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    Error404Component,
    SignInComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    VerifyAccoutComponent,
    SignUpComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AdminModule,
    BrowserAnimationsModule,
    CodeInputModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
