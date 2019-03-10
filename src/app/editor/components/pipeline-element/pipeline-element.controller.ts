export class PipelineElementController {

    ImageChecker: any;
    ElementIconText: any;
    showImage: any;
    iconText: any;
    pipelineElement: any;
    preview: any;
    iconSize: any;
    iconStandSize: any;
    iconUrl: any;
    RestApi: any;

    constructor(ImageChecker, ElementIconText, RestApi) {
        this.ImageChecker = ImageChecker;
        this.ElementIconText = ElementIconText;
        this.RestApi = RestApi;
        this.showImage = false;
    }

    $onInit() {
        this.iconText =  this.ElementIconText.getElementIconText(this.pipelineElement.name);
        this.checkImageAvailable();
    }

    checkImageAvailable() {
        if (this.pipelineElement.includesAssets) {
            this.fetchImage(this.makeAssetIconUrl())
        } else {
            this.fetchImage(this.pipelineElement.iconUrl);
        }
    }

    fetchImage(imageUrl) {
        this.ImageChecker.imageExists(imageUrl, (exists) => {
            this.iconUrl = imageUrl;
            this.showImage = exists;
        })
    }

    makeAssetIconUrl() {
        return this.RestApi.getAssetUrl(this.pipelineElement.appId) +"/icon";
    }

    iconSizeCss() {
        if (this.iconSize) {
            return 'width:35px;height:35px;';
        }
        else if (this.preview) {
            return 'width:50px;height:50px;';
        } else if (this.iconStandSize) {
            return 'width:50px;height:50px;margin-top:-5px;'
        } else {
            return 'width:70px;height:70px;';
        }
    }
}

PipelineElementController.$inject=['ImageChecker', 'ElementIconText', 'RestApi'];