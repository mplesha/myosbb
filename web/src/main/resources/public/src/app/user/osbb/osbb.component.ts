import {Component, OnInit} from '@angular/core';
import {CORE_DIRECTIVES} from '@angular/common';
import {MODAL_DIRECTIVES, BS_VIEW_PROVIDERS} from 'ng2-bootstrap/ng2-bootstrap';
import {ModalDirective} from "ng2-bootstrap/ng2-bootstrap";
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';

import {IOsbb, Osbb} from "../../../shared/models/osbb";
import { OsbbDTO } from './osbb';
import { OsbbService } from './osbb.service';
import { OsbbModalComponent } from './osbb_form/osbb-modal.component';
import { OsbbDelFormComponent } from './osbb_form/osbb-del-form.component';
import {CurrentUserService} from "../../../shared/services/current.user.service";
import {TranslatePipe} from "ng2-translate";
import {CapitalizeFirstLetterPipe} from "../../../shared/pipes/capitalize-first-letter";
import ApiService = require("../../../shared/services/api.service");
import {FILE_UPLOAD_DIRECTIVES, FileSelectDirective, FileDropDirective, FileUploader} from "ng2-file-upload/ng2-file-upload";

const attachmentUploadUrl = ApiService.serverUrl + '/restful/osbb';

@Component({
    selector: 'osbb',
    templateUrl: './src/app/user/osbb/osbb.component.html',
    styleUrls: ['./src/app/user/osbb/osbb.component.css'],
    providers: [OsbbService],
    directives: [MODAL_DIRECTIVES, CORE_DIRECTIVES, OsbbModalComponent, OsbbDelFormComponent],
    viewProviders: [BS_VIEW_PROVIDERS],
    pipes: [TranslatePipe, CapitalizeFirstLetterPipe]
})
export class OsbbComponent implements OnInit { 
    
    osbbArr:IOsbb[];
    updatedOsbb:IOsbb;
    order: boolean;

    constructor( private osbbService: OsbbService, private userService:CurrentUserService) { 
        this.osbbArr = [];
    }

    ngOnInit() {
        this.osbbService.getAllOsbb().then(osbbArr => this.osbbArr = osbbArr);
    }

    private initUpdatedOsbb(osbb:IOsbb): void {
        this.updatedOsbb = osbb;
    }

    createOsbb(osbbDTO:OsbbDTO): void {
        this.osbbService.addOsbb(osbbDTO.osbb)
        .then((osbb) => {
            let fileUploader = osbbDTO.logo;
             if(fileUploader.queue.length > 0) {
                fileUploader.queue[0].url = attachmentUploadUrl + '/' +  osbb.osbbId;
                fileUploader.authToken = 'Bearer ' + localStorage.getItem('access_token')
                fileUploader.queue[0].upload();
             }
            fileUploader.onCompleteAll = () =>  fileUploader.clearQueue();
            this.addOsbb(osbb);
        })
    }

    private addOsbb(osbb: IOsbb): void {
        this.osbbArr.unshift(osbb);
    }

    editOsbb(osbb:IOsbb): void {
        this.osbbService.editOsbb(osbb);
    }

    deleteOsbb(osbb:IOsbb): void {
        this.osbbService.deleteOsbb(osbb).then(osbb => this.deleteOsbbFromArr(osbb));
    }

     private deleteOsbbFromArr(osbb: IOsbb): void {
         let index = this.osbbArr.indexOf(osbb);
         if(index > -1) {
            this.osbbArr.splice(index, 1);
         }
    }

    getCreationDate(date:Date):string {
        return new Date(date).toLocaleString();
    }

    searchByNameOsbb(osbbName: string) {
        if(osbbName.trim()!=='') {
            this.osbbService.getAllOsbbByNameContaining(osbbName).then(osbbArr => this.osbbArr = osbbArr);
        } else {
             this.osbbService.getAllOsbb().then(osbbArr => this.osbbArr = osbbArr);
        }
    }

    toggleOrder() {
        if(this.order === false) {
            this.order = true;
        } else {
            this.order = false;
        }
    }

    sortBy(field:string): void {
        this.toggleOrder();
        this.osbbService.getAllOsbbByOrder(field, this.order).then(osbbArr => this.osbbArr = osbbArr);
    }
}