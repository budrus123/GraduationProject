import { ExamViewerPage } from './app.po';

describe('exam-viewer App', () => {
  let page: ExamViewerPage;

  beforeEach(() => {
    page = new ExamViewerPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
